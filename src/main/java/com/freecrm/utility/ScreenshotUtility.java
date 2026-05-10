package com.freecrm.utility;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.freecrm.base.Base;
import com.freecrm.reports.ExtentTestManager;

/**
 * Captures screenshots and returns them as a Base64-encoded string so they can
 * be embedded directly in the ExtentReports HTML report.
 */
public class ScreenshotUtility {

	private ScreenshotUtility() {
	}

	/**
	 * Takes a screenshot of the current browser window.
	 *
	 * @return Base64-encoded PNG string, or {@code null} if the driver is
	 *         unavailable.
	 */
	public static String captureBase64Screenshot() {
		WebDriver driver = Base.getDriver();
		if (driver == null) {
			return null;
		}
		try {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		} catch (Exception e) {
			System.err.println("[ScreenshotUtility] Failed to capture screenshot: " + e.getMessage());
			return null;
		}
	}

	public static void takeScreenshot() {
		WebDriver driver = Base.getDriver();
		if (driver == null) {

		}
		try {
			String projectPath = System.getProperty("user.dir");
			File screenshotDir = new File(projectPath + "/src/main/java/screenshots");

			// AUTO-CREATE: Build the folder if it's missing
			if (!screenshotDir.exists()) {
				screenshotDir.mkdirs();
				System.out.println("Created new screenshots directory.");
			}

			// Grab the currently running TestNG test method name
			String testName = "screenshot";
			if (Reporter.getCurrentTestResult() != null) {
				testName = Reporter.getCurrentTestResult().getMethod().getMethodName();
			}

			TakesScreenshot screenshot = (TakesScreenshot) driver;
			File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
			File destinationFile = new File(
					projectPath + "/src/test/resources/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png");
			FileHandler.copy(sourceFile, destinationFile);

		} catch (Exception e) {
			System.err.println("[ScreenshotUtility] Failed to capture screenshot: " + e.getMessage());

		}
	}

	/**
	 * One-liner helper: captures a screenshot AND logs it to the current ExtentTest
	 * at whatever Status/label you choose.
	 *
	 * <pre>
	 * // Usage examples inside any @Test or Page Object:
	 * ScreenshotUtility.logScreenshot(Status.INFO, "After clicking Login");
	 * ScreenshotUtility.logScreenshot(Status.PASS, "Order placed successfully");
	 * ScreenshotUtility.logScreenshot(Status.WARNING, "Slow network detected");
	 * ScreenshotUtility.logScreenshot(Status.FAIL, "Validation error visible");
	 * </pre>
	 *
	 * @param status  ExtentReports Status (INFO, PASS, WARNING, FAIL, …)
	 * @param message Label shown next to the screenshot thumbnail in the report
	 */
	public static void logScreenshot(Status status, String message) {
		String base64 = captureBase64Screenshot();
		if (base64 != null && ExtentTestManager.getTest() != null) {
			ExtentTestManager.getTest().log(status, message,
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
		}
	}

	/**
	 * Overload: logs step + value detail + screenshot in a single call.
	 * Replaces the need to call StepLogger and logScreenshot separately.
	 *
	 * <pre>
	 * ScreenshotUtility.logScreenshot(Status.FAIL, "Login failed", "URL: " + driver.getCurrentUrl());
	 * </pre>
	 */
	public static void logScreenshot(Status status, String step, String value) {
		String base64 = captureBase64Screenshot();
		if (base64 != null && ExtentTestManager.getTest() != null) {
			String icon = switch (status) {
				case PASS    -> "✔";
				case FAIL    -> "✘";
				case WARNING -> "⚠";
				default      -> "ℹ";
			};
			StringBuilder msg = new StringBuilder();
			msg.append("<b>").append(icon).append(" ").append(step).append("</b>");
			if (value != null && !value.isBlank()) {
				msg.append(" → <code>").append(value).append("</code>");
			}
			ExtentTestManager.getTest().log(status, msg.toString(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
		}
	}
}
