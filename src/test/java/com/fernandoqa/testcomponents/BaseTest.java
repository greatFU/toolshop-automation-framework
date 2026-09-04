package com.fernandoqa.testcomponents;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	public WebDriver getDriver() {
		return driver;
	}

	public List<Map<String, String>> getJsonDataToMap(String resourcePath) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {

			if (inputStream == null) {
				throw new IllegalArgumentException("Test data file not found: " + resourcePath);
			}

			return mapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>() {
			});
		}
	}

	public Object[][] getDataFromJson(String resourcePath) throws IOException {
		List<Map<String, String>> data = getJsonDataToMap(resourcePath);

		Object[][] result = new Object[data.size()][1];

		for (int i = 0; i < data.size(); i++) {
			result[i][0] = data.get(i);
		}
		return result;
	}

	protected WebDriver initializeDriver() throws IOException {
		Properties prop = new Properties();
		String configPath = System.getProperty("user.dir") + "/src/test/resources/config.properties";

		try (FileInputStream inputStream = new FileInputStream(configPath)) {
			prop.load(inputStream);
		}
		String baseUrlFromMaven = System.getProperty("baseUrl");

		baseUrl = (baseUrlFromMaven != null
		        ? baseUrlFromMaven
		        : prop.getProperty("baseUrl")).trim();

		String browserFromMaven = System.getProperty("browser");
		String browserName = browserFromMaven != null ? browserFromMaven : prop.getProperty("browser");
		browserName = browserName.trim().toLowerCase();

		boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

		WebDriver createdDriver;
		switch (browserName) {
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			if (headless) {
				options.addArguments("--headless=new");
				options.addArguments("--window-size=1920,1080");
			}
			createdDriver = new ChromeDriver(options);
			break;

		case "firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();

			if (headless) {
				firefoxOptions.addArguments("-headless");
				firefoxOptions.addArguments("--window-size=1920,1080");
			}

			createdDriver = new FirefoxDriver(firefoxOptions);
			break;

		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();

			if (headless) {
				edgeOptions.addArguments("--headless=new");
				edgeOptions.addArguments("--window-size=1920,1080");
			}

			createdDriver = new EdgeDriver(edgeOptions);
			break;

		default:
			throw new IllegalArgumentException("Unsupported browser: " + browserName);
		}

		if (!headless) {
			createdDriver.manage().window().maximize();
		}
		return createdDriver;

	}

}