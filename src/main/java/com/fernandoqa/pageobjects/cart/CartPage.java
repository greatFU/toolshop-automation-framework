package com.fernandoqa.pageobjects.cart;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

	private static final By PRODUCT_ROWS = By.cssSelector(".table-hover tbody tr");

	@FindBy(css = "td[data-test='cart-total']")
	private WebElement totalPrice;

	@FindBy(xpath = "//app-cart//p[normalize-space()='The cart is empty. Nothing to display.']")
	private WebElement emptyCartMessage;

	@FindBy(css = "button[data-test='proceed-1']")
	private WebElement proceedToCheckoutBtn;

	private void waitForCartState() {
		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfAllElementsLocatedBy(PRODUCT_ROWS),
				ExpectedConditions.visibilityOf(emptyCartMessage)));
	}

	private List<CartItemComponent> getCartItems() {

		waitForCartState();

		return driver.findElements(PRODUCT_ROWS).stream().filter(WebElement::isDisplayed)
				.map(row -> new CartItemComponent(driver, row)).toList();
	}

	private CartItemComponent findProductByNameNow(String productName) {

		return driver.findElements(PRODUCT_ROWS).stream().filter(WebElement::isDisplayed)
				.map(row -> new CartItemComponent(driver, row))
				.filter(product -> product.getProductName().equalsIgnoreCase(productName)).findFirst().orElse(null);
	}

	private CartItemComponent getProductByName(String productName) {

		return wait.until(driver -> {
			try {
				return findProductByNameNow(productName);

			} catch (StaleElementReferenceException | NoSuchElementException exception) {

				return null;
			}
		});
	}

	private void waitForProductUpdate(String productName, int expectedQuantity, BigDecimal previousLineTotal) {

		wait.until(driver -> {
			try {
				CartItemComponent product = findProductByNameNow(productName);

				if (product == null) {
					return false;
				}

				boolean quantityUpdated = product.getQuantity() == expectedQuantity;

				boolean lineTotalUpdated = product.getLineTotal().compareTo(previousLineTotal) != 0;

				return quantityUpdated && lineTotalUpdated;

			} catch (StaleElementReferenceException | NoSuchElementException exception) {

				return false;
			}
		});
	}

	private void waitForDisplayedTotalToMatchCalculatedTotal() {

		wait.until(driver -> {
			try {
				BigDecimal calculatedTotal = getCalculatedItemsTotal();

				BigDecimal displayedTotal = getDisplayedCartTotal();

				return calculatedTotal.compareTo(displayedTotal) == 0;

			} catch (StaleElementReferenceException | NoSuchElementException exception) {

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

	public BigDecimal getProductUnitPrice(String productName) {
		return getProductByName(productName).getUnitPrice();
	}

	public BigDecimal getCalculatedItemsTotal() {

		return getCartItems().stream().map(CartItemComponent::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
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

		BigDecimal previousLineTotal = product.getLineTotal();

		product.changeQuantity(quantity);

		waitForProductUpdate(productName, quantity, previousLineTotal);

		waitForDisplayedTotalToMatchCalculatedTotal();
	}

	public List<String> getProductNames() {

		return getCartItems().stream().map(CartItemComponent::getProductName).toList();
	}

	public boolean containsProduct(String productName) {

		return getProductNames().stream().anyMatch(name -> name.equalsIgnoreCase(productName));
	}

	public void removeProduct(String productName) {

		getProductByName(productName).remove();

		waitForCartState();

		if (!driver.findElements(PRODUCT_ROWS).isEmpty()) {
			waitForDisplayedTotalToMatchCalculatedTotal();
		}
	}

	public boolean isEmpty() {

		waitForCartState();

		return driver.findElements(PRODUCT_ROWS).isEmpty();
	}

	public CheckoutSignInPage proceedToCheckout() {

		waitForClickability(proceedToCheckoutBtn).click();

		return new CheckoutSignInPage(driver);
	}
}