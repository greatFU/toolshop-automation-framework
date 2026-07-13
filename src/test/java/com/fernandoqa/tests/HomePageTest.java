package com.fernandoqa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fernandoqa.testcomponents.BaseTest;

public class HomePageTest extends BaseTest {

	@Test
	public void homePageShouldLoad()
	{
		Assert.assertTrue(homePage.isLoaded(), 
				"Home page was not loaded");
	}
	
	@Test
	public void productsShouldBeDisplayed() {
	    Assert.assertTrue(
	        homePage.getDisplayedProductsCount() > 0,
	        "No products were displayed"
	    );
	}
}
