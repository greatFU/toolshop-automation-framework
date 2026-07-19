package com.fernandoqa.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fernandoqa.flows.ShoppingFlow;
import com.fernandoqa.pageobjects.CartPage;
import com.fernandoqa.pageobjects.ProductPage;
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

	@Test
	public void multipleProductsShouldBeAddedToCart() {
		List<String> productNames = List.of("Combination Pliers", "Pliers", "Bolt Cutters");
		ShoppingFlow shoppingFlow = new ShoppingFlow();
		CartPage cartPage = shoppingFlow.addProducts(homePage, productNames);
		List<String> addedProducts = cartPage.getProductNames();

		Assert.assertTrue(addedProducts.containsAll(productNames),
				"Expected products: " + productNames + ", but cart contained: " + addedProducts);

		Assert.assertEquals(addedProducts.size(), productNames.size(), "Unexpected number of products in cart");
	}

}
