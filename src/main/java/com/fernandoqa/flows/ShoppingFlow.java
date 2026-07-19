package com.fernandoqa.flows;

import java.util.List;

import com.fernandoqa.pageobjects.CartPage;
import com.fernandoqa.pageobjects.HomePage;
import com.fernandoqa.pageobjects.ProductPage;

public class ShoppingFlow {

	public CartPage addProducts(HomePage homePage, List<String> productNames) {
		if (productNames == null || productNames.isEmpty()) {
			throw new IllegalArgumentException("Product list cannot be null or empty");
		}

		HomePage currentHomePage = homePage;

		for (int i = 0; i < productNames.size(); i++) {
			String currentProductName = productNames.get(i);

			ProductPage productPage = currentHomePage.openProductByName(currentProductName);

			productPage.addToCart();

			if (i + 1 < productNames.size()) {
				currentHomePage = productPage.goToHomePage();
			} else {
				return productPage.goToCartPage();
			}

		}
		throw new IllegalStateException("Shopping flow ended unexpectedly");
	}

}
