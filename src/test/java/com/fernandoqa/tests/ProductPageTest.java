package com.fernandoqa.tests;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.fernandoqa.pageobjects.ProductPage;
import com.fernandoqa.pageobjects.cart.CartPage;
import com.fernandoqa.testcomponents.BaseTest;

public class ProductPageTest extends BaseTest {

	private static final String PRODUCT_NAME = "Claw Hammer";

	@Test
	public void selectedProductDetailsShouldOpen() {
		ProductPage productPage = homePage.openProductByName(PRODUCT_NAME);
		Assert.assertTrue(productPage.isLoaded(), "Product page was not loaded");

		Assert.assertEquals(productPage.getProductName(), PRODUCT_NAME, "Wrong product name was opened");
	}

	@Test
	public void productShouldBeAddedToCart() {
		CartPage cartPage = homePage.openProductByName(PRODUCT_NAME).addToCart().goToCartPage();
		Assert.assertTrue(cartPage.containsProduct(PRODUCT_NAME), "Product was not found in cart: " + PRODUCT_NAME);
	}
}
