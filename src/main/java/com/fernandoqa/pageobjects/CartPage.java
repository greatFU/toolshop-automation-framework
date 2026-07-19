package com.fernandoqa.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.fernandoqa.components.AbstractComponent;

public class CartPage extends AbstractComponent{

	public CartPage(WebDriver driver)
	{
		super(driver);
		PageFactory.initElements(driver, this);
	}
	@FindBy(css = "span[data-test='product-title']")
	private List<WebElement> cartProductNames;
	
	
	  private List<WebElement> waitForProductTitles() {
	        return wait.until(
	            ExpectedConditions.visibilityOfAllElements(cartProductNames)
	        );
	    }
	  
	    public List<String> getProductNames() {
	        return waitForProductTitles()
	                .stream()
	                .map(WebElement::getText)
	                .map(String::trim)
	                .toList();
	    }
	    
	    public boolean containsProduct(String expectedProductName) {
	        return getProductNames()
	                .stream()
	                .anyMatch(actualName ->
	                    actualName.equalsIgnoreCase(expectedProductName)
	                );
	    }
}
