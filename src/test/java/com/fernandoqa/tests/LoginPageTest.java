package com.fernandoqa.tests;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fernandoqa.pageobjects.AdminDashboardPage;
import com.fernandoqa.pageobjects.LoginPage;
import com.fernandoqa.testcomponents.BaseTest;

public class LoginPageTest extends BaseTest {

	
	@Test(dataProvider = "adminLoginData")
	public void adminShouldLoginWithValidCredentials(Map<String, String> input) {
		LoginPage loginPage = homePage.goToLoginPage();

		AdminDashboardPage dashboardPage = loginPage.submitAdminLogin(input.get("email"), input.get("password"));

		Assert.assertTrue(dashboardPage.isLoaded(), "Admin dashboard was not loaded");
	}
	
	@Test(dataProvider = "adminLoginData", groups = "knownBug")
	public void adminUserMenuShouldBeDisplayedAfterLogin(Map<String, String> input) {
	    LoginPage loginPage = homePage.goToLoginPage();

	    AdminDashboardPage dashboardPage =
	            loginPage.submitAdminLogin(input.get("email"), input.get("password"));

	    Assert.assertTrue(
	            dashboardPage.isUserMenuLoaded(),
	            "User menu was not displayed after successful admin login"
	    );
	}

	@Test(dataProvider = "invalidLoginData")
	public void invalidLoginShouldDisplayError(Map<String, String> input) {
		LoginPage loginPage = homePage.goToLoginPage();
		loginPage.submitInvalidLogin(input.get("email"), input.get("password"));

		Assert.assertEquals(loginPage.getErrorMessage(), input.get("expectedErrorMessage"), "Unexpected login - error message:");
	}
	
	
	
	//Test Data:
	@DataProvider
	public Object[][] adminLoginData() throws IOException
	{
		 return getDataFromJson("testdata/login/adminLoginData.json");

	}
	@DataProvider
	public Object[][] invalidLoginData() throws IOException
	{
		return getDataFromJson("testdata/login/invalidLoginData.json");
	}
}
