package com.fernandoqa.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.fernandoqa.components.AbstractComponent;

public class HomePage extends AbstractComponent {

	public HomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	private static final By PRODUCT_CARDS =
	        By.cssSelector("a.card");

	private static final By PRODUCT_NAME =
	        By.cssSelector("h5[data-test='product-name']");
	
	@FindBy(css = "a[data-test='nav-sign-in']")
	private WebElement signInBtn;

	public void open(String url) {
		driver.get(url);
	}

	private List<WebElement> waitForProducts() {
	    return wait.until(
	            ExpectedConditions
	                    .visibilityOfAllElementsLocatedBy(PRODUCT_CARDS)
	    );
	}
	
	public boolean isLoaded() {
		return !waitForProducts().isEmpty();
	}

	public int getDisplayedProductsCount() {
		return waitForProducts().size();
	}

	public LoginPage goToLoginPage() {
		waitForClickability(signInBtn).click();
		return new LoginPage(driver);
	}

	private WebElement getProductByName(String productName) {

	    return wait.until(driver -> {
	        try {
	            return driver.findElements(PRODUCT_CARDS)
	                    .stream()
	                    .filter(WebElement::isDisplayed)
	                    .filter(product ->
	                            product.findElement(PRODUCT_NAME)
	                                    .getText()
	                                    .trim()
	                                    .equalsIgnoreCase(productName))
	                    .findFirst()
	                    .orElse(null);

	        } catch (StaleElementReferenceException |
	                 org.openqa.selenium.NoSuchElementException exception) {
	            return null;
	        }
	    });
	}
	
	public ProductPage openProductByName(String productName)
	{
		getProductByName(productName).click();
		return new ProductPage(driver);
	}
	
}
