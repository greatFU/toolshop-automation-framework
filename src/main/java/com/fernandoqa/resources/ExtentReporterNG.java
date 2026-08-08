package com.fernandoqa.resources;

import java.nio.file.Path;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

public class ExtentReporterNG {

	public static ExtentReports getReportObject() {

		Path reportsDirectory = Path.of(System.getProperty("user.dir"), "reports");

		try {
			Files.createDirectories(reportsDirectory);
		} catch (IOException e) {
			throw new UncheckedIOException("Could not create reports directory", e);
		}

		Path reportPath = reportsDirectory.resolve("index.html");

		ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath.toString());

		reporter.config().setReportName("Web Automation Results");

		reporter.config().setDocumentTitle("Test Results");

		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Fernando Yuzepchuk");

		return extent;
	}
}
