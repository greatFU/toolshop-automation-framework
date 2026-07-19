package com.fernandoqa.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.fernandoqa.components.AbstractComponent;

public class ProductPage extends AbstractComponent {

	public ProductPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "h1[data-test='product-name']")
	private WebElement title;

	@FindBy(css = "button[data-test='add-to-cart']")
	private WebElement addToCartBtn;

	@FindBy(css = ".toast-success")
	private WebElement productAddedAlert;


	
	
	public boolean isLoaded() {
		waitForUrl("/product/");
		return waitForVisibility(title).isDisplayed();
	}

	public String getProductName() {
		return waitForVisibility(title).getText().trim();
	}

	private boolean isProductInStock() {
		waitForVisibility(addToCartBtn);
		return addToCartBtn.isEnabled();
	}

	public ProductPage addToCart() {
		if (!isProductInStock()) {
			throw new IllegalStateException(
					"Cannot add product to cart because it is out of stock: " + getProductName());
		}
			waitForClickability(addToCartBtn).click();
			waitForVisibility(productAddedAlert);
	        return this;

	}
	

}



