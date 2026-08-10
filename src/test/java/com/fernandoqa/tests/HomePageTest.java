package com.fernandoqa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fernandoqa.testcomponents.BaseTest;

public class HomePageTest extends BaseTest {

	@Test(groups = {"smoke", "regression"})
	public void homePageShouldLoad()
	{
		Assert.assertTrue(homePage.isLoaded(), 
				"Home page was not loaded");
	}
	
	@Test(groups = {"smoke", "regression"})
	public void productsShouldBeDisplayed() {
	    Assert.assertTrue(
	        homePage.getDisplayedProductsCount() > 0,
	        "No products were displayed"
	    );
	}
}
