package com.fernandoqa.components;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fernandoqa.pageobjects.HomePage;
import com.fernandoqa.pageobjects.cart.CartPage;

public abstract class AbstractComponent {

	protected final WebDriver driver;
	protected final WebDriverWait wait;

	@FindBy(css = "a[data-test='nav-cart']")
	private WebElement cartLink;
	
	@FindBy(css = "a[data-test='nav-home']")
	private WebElement homeLink;
	
	
	public AbstractComponent(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	protected WebElement waitForVisibility(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	protected WebElement waitForClickability(WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	protected List<WebElement> waitForListVisibility(List<WebElement> elements)
	{
		return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}
	
	protected void waitForUrl(String partialUrl)
	{
		wait.until(ExpectedConditions.urlContains(partialUrl));
	}
	
	public CartPage goToCartPage()
	{
		waitForClickability(cartLink).click();
		waitForUrl("/checkout");
		return new CartPage(driver);
	}
	
	public HomePage goToHomePage() {
	    waitForClickability(homeLink).click();
	    return new HomePage(driver);
	}


}
