package com.fernandoqa.pageobjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.fernandoqa.components.AbstractComponent;

public class AdminDashboardPage extends AbstractComponent {

	public AdminDashboardPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	    @FindBy(css = "h1[data-test='page-title']")
	    private WebElement dashboardTitle;

	    @FindBy(css = "button[data-test='nav-menu']")
	    private WebElement userMenu;

	    public boolean isLoaded() {
	        return wait.until(ExpectedConditions.urlContains("/admin/dashboard")) && waitForVisibility(dashboardTitle).isDisplayed();
	    }

	    public boolean isUserMenuDisplayed() {
	        return waitForClickability(userMenu).isDisplayed();
	    }
}
