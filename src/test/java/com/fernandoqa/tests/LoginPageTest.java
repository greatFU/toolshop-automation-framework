package com.fernandoqa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fernandoqa.pageobjects.AdminDashboardPage;
import com.fernandoqa.pageobjects.LoginPage;
import com.fernandoqa.testcomponents.BaseTest;

public class LoginPageTest extends BaseTest {

	
	@Test()
	public void adminShouldLoginWithValidCredentials() {
		LoginPage loginPage = homePage.goToLoginPage();

		AdminDashboardPage dashboardPage = loginPage.submitValidLogin("admin@practicesoftwaretesting.com",
				"welcome01");

		Assert.assertTrue(dashboardPage.isLoaded(), "Admin dashboard was not loaded");

		Assert.assertTrue(dashboardPage.isUserMenuDisplayed(), "User menu was not displayed after successful login");
	}

	@Test
	public void invalidLoginShouldDisplayError() {
		LoginPage loginPage = homePage.goToLoginPage();
		loginPage.submitInvalidLogin("Not@Exist.com", "Not@Exist1");

		Assert.assertEquals(loginPage.getErrorMessage(), "Invalid email or password", "Unexpected login error message");
	}
}
