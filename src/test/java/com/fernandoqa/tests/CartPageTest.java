package com.fernandoqa.tests;

import java.math.BigDecimal;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fernandoqa.flows.ShoppingFlow;
import com.fernandoqa.pageobjects.BillingAddressPage;
import com.fernandoqa.pageobjects.CheckoutSignInPage;
import com.fernandoqa.pageobjects.cart.CartPage;
import com.fernandoqa.testcomponents.BaseTest;

public class CartPageTest extends BaseTest {

	@Test
	public void addedProductsShouldBeDisplayed() {
		List<String> products = List.of("Combination Pliers", "Hammer");
		ShoppingFlow shoppingFlow = new ShoppingFlow();
		CartPage cartPage = shoppingFlow.addProducts(homePage, products);

		List<String> actualProducts = cartPage.getProductNames();
		
		Assert.assertEquals(actualProducts.size(), products.size(), "Unexpected number of products in cart");
		Assert.assertTrue(actualProducts.containsAll(products),
				"Expected products: " + products + ", but cart contained: " + actualProducts);
	}

	@Test
	public void selectedItemShouldBeDeleted() {
		String productToRemove = "Hammer";
		String productToKeep = "Slip Joint Pliers";

		List<String> products = List.of(productToKeep, productToRemove);
		ShoppingFlow shoppingFlow = new ShoppingFlow();
		CartPage cartPage = shoppingFlow.addProducts(homePage, products);

		cartPage.removeProduct(productToRemove);
		Assert.assertFalse(cartPage.containsProduct(productToRemove),
				productToRemove + " is still displayed after removal");
		Assert.assertTrue(cartPage.containsProduct(productToKeep), productToKeep + " should be displayed in the cart");
	}

	@Test
	public void productLineTotalShouldChangeWithQuantity() {
		String productName = "Pliers";
		int newQuantity = 2;

		CartPage cartPage = homePage.openProductByName(productName).addToCart().goToCartPage();

		BigDecimal unitPrice = cartPage.getProductUnitPrice(productName);

		cartPage.changeProductQuantity(productName, newQuantity);

		Assert.assertEquals(cartPage.getProductQuantity(productName), newQuantity, "Product quantity was not updated");

		BigDecimal expectedLineTotal = unitPrice.multiply(BigDecimal.valueOf(newQuantity));

		Assert.assertEquals(cartPage.getProductLineTotal(productName).compareTo(expectedLineTotal), 0,
				"Product line total was not recalculated correctly");
	}

	@Test
	public void displayedCartTotalShouldMatchCalculatedTotal() {
		List<String> products = List.of("Claw Hammer", "Combination Pliers");

		ShoppingFlow shoppingFlow = new ShoppingFlow();
		CartPage cartPage = shoppingFlow.addProducts(homePage, products);

		BigDecimal calculatedTotal = cartPage.getCalculatedItemsTotal();

		BigDecimal displayedTotal = cartPage.getDisplayedCartTotal();

		Assert.assertEquals(displayedTotal.compareTo(calculatedTotal), 0,
				"Displayed cart total does not match " + "the sum of product line totals");
	}
	
	@Test
	public void emptyCartMessageShouldBeDisplayedAfterRemovingLastProduct() {
	    String productName = "Combination Pliers";

	    CartPage cartPage = homePage
	            .openProductByName(productName)
	            .addToCart()
	            .goToCartPage();

	    cartPage.removeProduct(productName);

	    Assert.assertTrue(
	            cartPage.isEmpty(),
	            "Cart should be empty after removing the last product"
	    );
	}

	@Test
	public void loggedInUserShouldProceedToBillingAddress() {
		CheckoutSignInPage checkoutSignInPage = homePage.goToLoginPage()
				.submitCustomerLogin("customer@practicesoftwaretesting.com", "welcome01").goToHomePage()
				.openProductByName("Combination Pliers").addToCart().goToCartPage().proceedToCheckout();

		Assert.assertTrue(checkoutSignInPage.isLoggedInMessageDisplayed(),
				"Logged-in checkout message was not displayed");

		BillingAddressPage billingAddressPage = checkoutSignInPage.continueAsLoggedInUser();

		Assert.assertTrue(billingAddressPage.isLoaded(), "Billing address step was not displayed");
	}
}
