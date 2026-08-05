package com.fernandoqa.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public final class Screenshot {

	private Screenshot(){
	}
	
	public static String takeScreenshot(WebDriver driver,String testName) throws IOException {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

		if (driver instanceof TakesScreenshot screenshotDriver) {
			File temporaryScreenshot = screenshotDriver.getScreenshotAs(OutputType.FILE);
			Path screenshotsDirectory = Path.of(System.getProperty("user.dir"), "screenshots");
				Files.createDirectories(screenshotsDirectory);
				Path destination = screenshotsDirectory.resolve(testName + "-" + now.format(customFormat) + ".png");
				Files.copy(temporaryScreenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
				return destination.toAbsolutePath().toString();
		}throw new IllegalStateException("Driver does not support screenshots");

	}
}
