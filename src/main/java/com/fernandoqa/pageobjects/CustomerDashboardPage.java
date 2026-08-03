package com.fernandoqa.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.fernandoqa.components.AbstractComponent;

public class CustomerDashboardPage extends AbstractComponent {

	public CustomerDashboardPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "h1[data-test='page-title']")
	private WebElement dashboardTitle;

	@FindBy(css = "button[data-test='nav-menu']")
	private WebElement userMenu;

	public boolean isLoaded() {
		waitForUrl("/account");
		return waitForVisibility(dashboardTitle).isDisplayed();
	}

	public boolean isUserMenuDisplayed() {
		return waitForClickability(userMenu).isDisplayed();
	}
}
