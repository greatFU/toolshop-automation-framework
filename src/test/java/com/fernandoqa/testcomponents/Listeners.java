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

public class Listeners implements ITestListener {
	ExtentTest test;
	ExtentReports extent = ExtentReporterNG.getReportObject();
	ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>(); // Thread safe

	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);// unique thread id(every test)->test
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().log(Status.PASS, "Test Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().fail(result.getThrowable());

		String testName = result.getMethod().getMethodName();
		Object testInstance = result.getInstance();
		Reporter.log("FAILED:" + testName, true);

		if (testInstance instanceof BaseTest baseTest) {
			try {
				WebDriver driver = baseTest.getDriver();
				String screenshotPath = Screenshot.takeScreenshot(driver, testName);
				extentTest.get().addScreenCaptureFromPath(screenshotPath, result.getMethod().getMethodName());
			} catch (Exception e) {
				Reporter.log("SCREENSHOT ERROR:" + e.getMessage(), true);
			}
		} else {
			Reporter.log("SCREENSHOT SKIPPED: test does not extend BaseTest",true);
		}

	}

	@Override
	public void onTestSkipped(ITestResult result) 
	{
		Throwable throwable = result.getThrowable();
	        if (throwable != null) {
	        	extentTest.get().skip(throwable);
	        } else {
	        	extentTest.get().skip("Test skipped");
	        }
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		extent.flush();

	}

}
