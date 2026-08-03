package com.fernandoqa.pageobjects.cart;

import java.math.BigDecimal;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fernandoqa.utils.PriceUtils;

class CartItemComponent {

	private final WebElement rootElement;
	private final WebDriverWait wait;

	CartItemComponent(WebDriver driver, WebElement rootElement) {
		this.rootElement = rootElement;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	String getProductName() {
		return rootElement.findElement(By.cssSelector("span[data-test='product-title']")).getText()
				.replace('\u00A0', ' ').trim();
	}

	int getQuantity() {
		String quant = rootElement.findElement(By.cssSelector("input[data-test='product-quantity']"))
				.getDomProperty("value");
		return Integer.parseInt(quant);
	}

	void changeQuantity(int quantity) {
		if (quantity < 1) {
			throw new IllegalArgumentException("Quantity must be greater than zero");
		}

		if (getQuantity() == quantity) {
			return;
		}

		WebElement quantityField = rootElement.findElement(By.cssSelector("input[data-test='product-quantity']"));

		quantityField.clear();
		quantityField.sendKeys(String.valueOf(quantity), Keys.TAB);

		wait.until(ExpectedConditions.stalenessOf(rootElement));
	}

	BigDecimal getLineTotal() {
		String price = rootElement.findElement(By.cssSelector("span[data-test='line-price']")).getText();
		return PriceUtils.parsePrice(price);
	}

	void remove() {
		WebElement removeButton = rootElement.findElement(By.cssSelector(".btn-danger"));

		removeButton.click();

		wait.until(ExpectedConditions.stalenessOf(rootElement));
	}

	BigDecimal getUnitPrice() {
		String priceStr = rootElement.findElement(By.cssSelector("span[data-test='product-price']")).getText();
		return PriceUtils.parsePrice(priceStr);
	}

}
