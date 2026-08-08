package com.fernandoqa.testcomponents;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.fernandoqa.resources.ExtentReporterNG;
import com.fernandoqa.utils.Screenshot;

public class TestListener implements ITestListener {
	private final ExtentReports extent = ExtentReporterNG.getReportObject();
	private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>(); // Thread safe

	@Override
	public void onTestStart(ITestResult result) {
		ExtentTest test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().log(Status.PASS, "Test Passed");
		extentTest.remove();
	}

	@Override
	public void onTestFailure(ITestResult result) {
		try {
			extentTest.get().fail(result.getThrowable());

			String testName = result.getMethod().getMethodName();
			Object testInstance = result.getInstance();
			Reporter.log("FAILED: " + testName, true);

			if (testInstance instanceof BaseTest baseTest) {
				try {
					WebDriver driver = baseTest.getDriver();
					String screenshotPath = Screenshot.takeScreenshot(driver, testName);
					extentTest.get().addScreenCaptureFromPath(screenshotPath, testName);
				} catch (Exception e) {
					Reporter.log("SCREENSHOT ERROR: " + e.getMessage(), true);
				}
			} else {
				Reporter.log("SCREENSHOT SKIPPED: test does not extend BaseTest", true);
			}
		} finally {
			extentTest.remove();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
	    // if test skipped before "onTestStart" - @BeforeMethod is failed , ExtentTest = null
	    if (extentTest.get() == null) {
	        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
	        extentTest.set(test);
	    }

		try {
	        Throwable throwable = result.getThrowable();
	        if (throwable != null) {
	            extentTest.get().skip(throwable);
	        } else {
	            extentTest.get().skip("Test skipped");
	        }
	    } finally {
	        extentTest.remove();
	    }
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		extent.flush();

	}

}
