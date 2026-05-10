package com.freecrm.reports;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.freecrm.utility.ScreenshotUtility;

/**
 * TestNG listener that hooks into the test lifecycle to build a rich
 * ExtentReports HTML report with screenshots on failure/skip.
 */
public class ExtentReportsListener implements ITestListener {

	// ── Suite-level ────────────────────────────────────────────────────────

	@Override
	public void onStart(ITestContext context) {
		String suiteName   = context.getSuite().getName();

		// Only what CANNOT be auto-detected is defined as <parameter> in the TestNG XML.
		// browser, OS, hostname, CI/CD, Java, environment → all auto-detected.
		String author      = context.getSuite().getParameter("author");
		String application = context.getSuite().getParameter("application");

		ExtentReportManager.getInstance(suiteName, author, application);
		System.out.println("[ExtentReports] Suite started: " + suiteName);
	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentReportManager.flush();
		System.out.println("[ExtentReports] Suite finished – report flushed.");
	}

	// ── Test-level ─────────────────────────────────────────────────────────

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String description = result.getMethod().getDescription();
		String[] groups = result.getMethod().getGroups();

		ExtentTest test = ExtentTestManager.startTest(testName,
				(description != null && !description.isBlank()) ? description : testName);

		// Tag groups as categories
		for (String group : groups) {
			test.assignCategory(group);
		}

		// Log the class name as author
		test.assignAuthor(result.getTestClass().getName());

		test.log(Status.INFO, "▶ Started: <b>" + testName + "</b> | Class: " + result.getTestClass().getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentTest test = ExtentTestManager.getTest();
		if (test != null) {
			test.log(Status.PASS, "✔ Test PASSED: <b>" + result.getMethod().getMethodName() + "</b>");
		}
		ExtentTestManager.endTest();
	}

	@Override
	public void onTestFailure(ITestResult result) {
		ExtentTest test = ExtentTestManager.getTest();
		if (test != null) {
			// Log the exception
			test.log(Status.FAIL, "✘ Test FAILED: <b>" + result.getMethod().getMethodName() + "</b>");
			test.log(Status.FAIL, result.getThrowable());

			// Embed screenshot
			String base64 = ScreenshotUtility.captureBase64Screenshot();
			if (base64 != null) {
				test.fail("Screenshot on failure:",
						MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			}
		}
		ExtentTestManager.endTest();
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentTest test = ExtentTestManager.getTest();
		if (test == null) {
			// Test may have been skipped before onTestStart fired
			test = ExtentTestManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
		}
		test.log(Status.SKIP, "⚠ Test SKIPPED: <b>" + result.getMethod().getMethodName() + "</b>");
		if (result.getThrowable() != null) {
			test.log(Status.SKIP, result.getThrowable());
		}

		// Optionally capture screenshot on skip too
		String base64 = ScreenshotUtility.captureBase64Screenshot();
		if (base64 != null) {
			test.skip("Screenshot on skip:", MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
		}

		ExtentTestManager.endTest();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// Not used
	}
}
