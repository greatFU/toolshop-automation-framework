package com.fernandoqa.pageobjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.fernandoqa.components.AbstractComponent;

public class LoginPage extends AbstractComponent {
	
	public LoginPage(WebDriver driver)
	{
		super(driver);
		PageFactory.initElements(driver, this);
	}
	

	
	@FindBy(css = "input[data-test='email']")
	private WebElement emailField;
	
	@FindBy(css = "input[data-test='password']")
	private WebElement passwordField;
	
	@FindBy(css = "input[data-test='login-submit']")
	private WebElement loginSubmit;
	
	@FindBy(css = ".help-block")
	private WebElement errorMessage;

	
	
	
	private void submitLogin(String email, String password)
	{
		waitForClickability(emailField).clear();
		emailField.sendKeys(email);
		
		passwordField.clear();
		passwordField.sendKeys(password);
		
		waitForClickability(loginSubmit).click();
	}
	
	public AdminDashboardPage submitAdminLogin(String email, String password)
	{
		submitLogin(email,password);
		return new AdminDashboardPage(driver);
	}
	
	public LoginPage submitInvalidLogin(String email, String password) {
		submitLogin(email, password);
        return this;
    }

	public String getErrorMessage()
	{
		return waitForVisibility(errorMessage)
        .getText();
	}
	    
}
