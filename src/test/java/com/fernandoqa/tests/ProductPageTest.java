package com.fernandoqa.tests;


import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fernandoqa.pageobjects.ProductPage;
import com.fernandoqa.pageobjects.cart.CartPage;
import com.fernandoqa.testcomponents.BaseTest;

public class ProductPageTest extends BaseTest {


	@Test(dataProvider = "productsToAddData")
	public void selectedProductDetailsShouldOpen(Map<String,String> input) {
		ProductPage productPage = homePage.openProductByName(input.get("productName"));
		Assert.assertTrue(productPage.isLoaded(), "Product page was not loaded");

		Assert.assertEquals(productPage.getProductName(), input.get("productName"), "Wrong product name was opened");
	}

	@Test(dataProvider = "productsToAddData")
	public void productShouldBeAddedToCart(Map<String,String> input) {
		CartPage cartPage = homePage.openProductByName(input.get("productName")).addToCart().goToCartPage();
		Assert.assertTrue(cartPage.containsProduct(input.get("productName")), "Product was not found in cart: " + input.get("productName"));
	}
	
	@DataProvider
	public Object[][] productsToAddData() throws IOException
	{
		return getDataFromJson("testdata/product/productsToAddData.json");
	}
	
}
