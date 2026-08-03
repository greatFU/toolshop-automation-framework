package com.fernandoqa.pageobjects;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.fernandoqa.components.AbstractComponent;

public class CheckoutSignInPage extends AbstractComponent {

	public CheckoutSignInPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//p[contains(normalize-space(), 'you are already logged in')]")
	private WebElement loggedInMessage;

	@FindBy(css = "button[data-test='proceed-2']")
	private WebElement proceedToCheckoutBtn;

	public boolean isLoggedInMessageDisplayed() {
		try {
			return waitForVisibility(loggedInMessage).isDisplayed();
		} catch (TimeoutException exception) {
			return false;
		}
	}

	public BillingAddressPage continueAsLoggedInUser() {
		waitForClickability(proceedToCheckoutBtn).click();
		return new BillingAddressPage(driver);
	}
}
