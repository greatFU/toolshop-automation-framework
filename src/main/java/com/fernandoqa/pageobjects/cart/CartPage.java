package com.fernandoqa.pageobjects.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.fernandoqa.components.AbstractComponent;
import com.fernandoqa.pageobjects.CheckoutSignInPage;
import com.fernandoqa.utils.PriceUtils;

public class CartPage extends AbstractComponent {

	public CartPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".table-hover tbody tr")
	private List<WebElement> productRows;

	@FindBy(css = "td[data-test='cart-total']")
	private WebElement totalPrice;

	@FindBy(xpath = "//app-cart//p[normalize-space()='The cart is empty. Nothing to display.']")
	private WebElement emptyCartMessage;
	
	@FindBy(css = "button[data-test='proceed-1']")
	private WebElement proceedToCheckoutBtn;
	
	
	private void waitForCartState() {
	    wait.until(ExpectedConditions.or(
	            ExpectedConditions.visibilityOfAllElements(productRows),
	            ExpectedConditions.visibilityOf(emptyCartMessage)
	    ));
	}
	
	private List<CartItemComponent> getCartItems() {
		waitForCartState();
		return productRows.stream().map(row -> new CartItemComponent(driver, row)).toList();
	}

	private CartItemComponent getProductByName(String productName) {
		return getCartItems().stream().filter(product -> product.getProductName().equalsIgnoreCase(productName))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException("Product was not found in cart: " + productName));
	}

	private void waitForDisplayedTotalToMatchCalculatedTotal() {
	    wait.until(driver -> {
	        try {
	            BigDecimal calculatedTotal =
	                    getCalculatedItemsTotal();

	            BigDecimal displayedTotal =
	                    getDisplayedCartTotal();

	            return calculatedTotal
	                    .compareTo(displayedTotal) == 0;

	        } catch (StaleElementReferenceException exception) {
	            return false;
	        }
	    });
	}

	public int getProductQuantity(String productName) {
		return getProductByName(productName).getQuantity();
	}

	public BigDecimal getProductLineTotal(String productName) {
		return getProductByName(productName).getLineTotal();
	}

	public BigDecimal getCalculatedItemsTotal() {
		return getCartItems().stream().map(CartItemComponent::getLineTotal).reduce(BigDecimal.ZERO, (BigDecimal::add));
	}

	public BigDecimal getDisplayedCartTotal() {

		String totalText = waitForVisibility(totalPrice).getText();

		return PriceUtils.parsePrice(totalText);
	}

	public void changeProductQuantity(String productName, int quantity) {
		CartItemComponent product = getProductByName(productName);
		if (quantity == product.getQuantity()) {
			return;
		}		
		product.changeQuantity(quantity);
		waitForDisplayedTotalToMatchCalculatedTotal();
	}

	public List<String> getProductNames() {
		return getCartItems().stream().map(CartItemComponent::getProductName).toList();
	}

	public boolean containsProduct(String productName) {
		return getProductNames().stream().anyMatch(name -> name.equalsIgnoreCase(productName));
	}

	public BigDecimal getProductUnitPrice(String productName)
	{
		return getProductByName(productName).getUnitPrice();
	}
	public void removeProduct(String productName) {
		getProductByName(productName).remove();
		waitForCartState();
		if(!productRows.isEmpty()) {
			waitForDisplayedTotalToMatchCalculatedTotal();
		}
	}
	
	public boolean isEmpty() {
	    waitForCartState();
	    return productRows.isEmpty();
	}

	public CheckoutSignInPage proceedToCheckout() {
	    waitForClickability(proceedToCheckoutBtn).click();
	    return new CheckoutSignInPage(driver);
	}
}
