package com.fernandoqa.testcomponents;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fernandoqa.pageobjects.HomePage;

public class BaseTest {

	protected WebDriver driver;
	protected HomePage homePage;
	protected String baseUrl;

	@BeforeMethod(alwaysRun = true) 
	public void launchApplication() throws IOException {
		driver = initializeDriver();

		homePage = new HomePage(driver);
		homePage.open(baseUrl);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		if (driver != null)
			driver.quit();
	}

	public WebDriver initializeDriver() throws IOException {
		Properties prop = new Properties();
		String configPath = System.getProperty("user.dir") + "/src/test/resources/config.properties";

		try (FileInputStream inputStream = new FileInputStream(configPath)){
			prop.load(inputStream);
			}
		
		baseUrl = prop.getProperty("baseUrl");
		
		String browserName = prop.
				getProperty("browser").
				trim().toLowerCase();

		switch(browserName) {
			case "chrome":
				driver = new ChromeDriver();
				break;
				
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browserName);
		}
		
		driver.manage().window().maximize();
		return driver;
		
	}

}
