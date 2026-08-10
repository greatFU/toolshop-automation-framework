package com.fernandoqa.tests;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fernandoqa.flows.ShoppingFlow;
import com.fernandoqa.pageobjects.BillingAddressPage;
import com.fernandoqa.pageobjects.CheckoutSignInPage;
import com.fernandoqa.pageobjects.cart.CartPage;
import com.fernandoqa.testcomponents.BaseTest;

public class CartPageTest extends BaseTest {

	@Test(groups = {"smoke", "regression"})
	public void addedProductsShouldBeDisplayed() {
		List<String> products = List.of("Combination Pliers", "Hammer");
		ShoppingFlow shoppingFlow = new ShoppingFlow();
		CartPage cartPage = shoppingFlow.addProducts(homePage, products);

		List<String> actualProducts = cartPage.getProductNames();

		Assert.assertEquals(actualProducts.size(), products.size(), "Unexpected number of products in cart");
		Assert.assertTrue(actualProducts.containsAll(products),
				"Expected products: " + products + ", but cart contained: " + actualProducts);
	}

	@Test(dataProvider = "removeProductData", groups = "regression")
	public void selectedItemShouldBeDeleted(Map<String, String> input) {

		List<String> products = List.of(input.get("productToKeep"), input.get("productToRemove"));
		ShoppingFlow shoppingFlow = new ShoppingFlow();
		CartPage cartPage = shoppingFlow.addProducts(homePage, products);

		cartPage.removeProduct(input.get("productToRemove"));
		Assert.assertFalse(cartPage.containsProduct(input.get("productToRemove")),
				input.get("productToRemove") + " is still displayed after removal");
		Assert.assertTrue(cartPage.containsProduct(input.get("productToKeep")),
				input.get("productToKeep") + " should be displayed in the cart");
	}

	@Test(dataProvider = "productQuantityData", groups = "regression")
	public void productLineTotalShouldChangeWithQuantity(Map<String, String> input) {
		String productName = input.get("productName");

		int newQuantity = Integer.parseInt(input.get("newQuantity"));

		CartPage cartPage = homePage.openProductByName(productName).addToCart().goToCartPage();

		BigDecimal unitPrice = cartPage.getProductUnitPrice(productName);

		cartPage.changeProductQuantity(productName, newQuantity);

		Assert.assertEquals(cartPage.getProductQuantity(productName), newQuantity, "Product quantity was not updated");

		BigDecimal expectedLineTotal = unitPrice.multiply(BigDecimal.valueOf(newQuantity));

		Assert.assertEquals(cartPage.getProductLineTotal(productName).compareTo(expectedLineTotal), 0,
				"Product line total was not recalculated correctly");
	}

	@Test(groups = "regression")
	public void displayedCartTotalShouldMatchCalculatedTotal() {
		List<String> products = List.of("Claw Hammer", "Combination Pliers");

		ShoppingFlow shoppingFlow = new ShoppingFlow();
		CartPage cartPage = shoppingFlow.addProducts(homePage, products);

		BigDecimal calculatedTotal = cartPage.getCalculatedItemsTotal();

		BigDecimal displayedTotal = cartPage.getDisplayedCartTotal();

		Assert.assertEquals(displayedTotal.compareTo(calculatedTotal), 0,
				"Displayed cart total does not match " + "the sum of product line totals");
	}

	@Test(groups = "regression")
	public void emptyCartMessageShouldBeDisplayedAfterRemovingLastProduct() {
		String productName = "Combination Pliers";

		CartPage cartPage = homePage.openProductByName(productName).addToCart().goToCartPage();

		cartPage.removeProduct(productName);

		Assert.assertTrue(cartPage.isEmpty(), "Cart should be empty after removing the last product");
	}

	@Test(dataProvider = "customerCheckoutData", groups = {"smoke", "regression"})
	public void loggedInUserShouldProceedToBillingAddress(Map<String, String> input) {
		CheckoutSignInPage checkoutSignInPage = homePage.goToLoginPage()
				.submitCustomerLogin(input.get("email"), input.get("password")).goToHomePage()
				.openProductByName(input.get("productName")).addToCart().goToCartPage().proceedToCheckout();

		Assert.assertTrue(checkoutSignInPage.isLoggedInMessageDisplayed(),
				"Logged-in checkout message was not displayed");

		BillingAddressPage billingAddressPage = checkoutSignInPage.continueAsLoggedInUser();

		Assert.assertTrue(billingAddressPage.isLoaded(), "Billing address step was not displayed");
	}

	@DataProvider
	public Object[][] removeProductData() throws IOException {
		return getDataFromJson("testdata/cart/removeProductData.json");
	}

	@DataProvider
	public Object[][] productQuantityData() throws IOException {

		return getDataFromJson("testdata/cart/productQuantityData.json");
	}
	@DataProvider
	public Object[][] customerCheckoutData() throws IOException {

		return getDataFromJson("testdata/login/customerLoginData.json");
	}
	
	
}
