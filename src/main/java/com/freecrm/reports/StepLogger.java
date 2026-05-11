package com.freecrm.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.freecrm.base.Base;
import com.freecrm.utility.ScreenshotUtility;

/**
 * Convenience class for logging named steps with values into the current
 * thread's ExtentReports test node from anywhere (tests, page objects,
 * utilities).
 *
 * <p>
 * Example:
 * 
 * <pre>
 * StepLogger.info("Navigate to login page", driver.getCurrentUrl());
 * StepLogger.pass("Login successful", "Redirected to: " + driver.getTitle());
 * StepLogger.fail("Element not found", "Locator: " + locator.toString());
 * </pre>
 */
public class StepLogger {

	private StepLogger() {
	}

	/** Log an informational step. */
	public static void info(String step) {
		log(Status.INFO, step, null);
	}

	/** Log an informational step with a detail value. */
	public static void info(String step, String value) {
		log(Status.INFO, step, value);
	}

	/** Log a passing step. */
	public static void pass(String step) {
		log(Status.PASS, step, null);
	}

	/** Log a passing step with a detail value. */
	public static void pass(String step, String value) {
		log(Status.PASS, step, value);
	}

	/** Log a failing step. */
	public static void fail(String step) {
		log(Status.FAIL, step, null);
	}

	/** Log a failing step with a detail value. */
	public static void fail(String step, String value) {
		log(Status.FAIL, step, value);
	}

	/** Log a warning/skip step. */
	public static void warn(String step) {
		log(Status.WARNING, step, null);
	}

	public static void warn(String step, String value) {
		log(Status.WARNING, step, value);
	}

	public static void skip(String step) {
		log(Status.SKIP, step, null);
	}

	public static void skip(String step, String value) {
		log(Status.SKIP, step, value);
	}

	// ── Internal ──────────────────────────────────────────────────────────

	private static void log(Status status, String step, String value) {
		ExtentTest test = ExtentTestManager.getTest();
		if (test == null)
			return;

		String icon = switch (status) {
		case PASS -> "✔";
		case FAIL -> "✘";
		case WARNING -> "⚠";
		case SKIP -> "⚠";
		default -> "ℹ";
		};

		StringBuilder msg = new StringBuilder();
		msg.append("<b>").append(icon).append(" ").append(step).append("</b>");
		if (value != null && !value.isBlank()) {
			msg.append(" → <code>").append(value).append("</code>");
		}

		if ("true".equalsIgnoreCase(Base.getConfig("screenshotEachStepLogger"))) {
			// screenshot log already includes the formatted message — no plain log needed
			ScreenshotUtility.logScreenshot(status, step, value != null ? value : null);
		} else {
			test.log(status, msg.toString());
		}
	}
}