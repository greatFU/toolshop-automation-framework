package com.fernandoqa.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.fernandoqa.components.AbstractComponent;

public class HomePage extends AbstractComponent{
	
	public HomePage(WebDriver driver)
	{
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(css = "a[data-test='nav-sign-in']")
	private WebElement signInBtn;
	
	@FindBy(css = "a.card")
	private List<WebElement> products;
	
	public void open(String url)
	{
		driver.get(url);
	}
	
	private List<WebElement> waitForProducts()
	{
		return wait.until(ExpectedConditions.visibilityOfAllElements(products));
	}
	
	 public boolean isLoaded() 
	 {
	        return !waitForProducts().isEmpty();
	 }

	    public int getDisplayedProductsCount() 
	    {
	        return waitForProducts().size();
	    }
	    
	    public LoginPage goToLoginPage() {
	        waitForClickability(signInBtn).click();
	        return new LoginPage(driver);
	    }
}
