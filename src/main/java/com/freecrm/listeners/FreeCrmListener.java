package com.freecrm.listeners;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import com.freecrm.reports.StepLogger;

public class FreeCrmListener implements WebDriverListener {

	// =========================================
	// NAVIGATION EVENTS
	// =========================================

	@Override
	public void beforeGet(WebDriver driver, String url) {
		System.out.println("[BEFORE NAVIGATE] Opening URL: " + url);
		StepLogger.info("Navigate to URL", url);
	}

	@Override
	public void afterGet(WebDriver driver, String url) {
		System.out.println("[AFTER NAVIGATE] Opened URL: " + driver.getCurrentUrl());
	}

	@Override
	public void beforeGetCurrentUrl(WebDriver driver) {
		System.out.println("[INFO] Attempting to fetch current URL");
	}

	@Override
	public void afterGetCurrentUrl(WebDriver driver, String result) {
		System.out.println("[INFO] Current URL: " + result);
	}

	@Override
	public void beforeGetTitle(WebDriver driver) {
		System.out.println("[INFO] Attempting to fetch page title");
	}

	@Override
	public void afterGetTitle(WebDriver driver, String result) {
		System.out.println("[INFO] Page Title: " + result);
	}

	// =========================================
	// CLICK EVENTS
	// =========================================

	@Override
	public void beforeClick(WebElement element) {
		System.out.println("--------------------------->" + element);
		String details = getElementDetails(element);
		System.out.println("[BEFORE CLICK] " + details);
		StepLogger.info("Click", details);
	}

	@Override
	public void afterClick(WebElement element) {
		System.out.println("[AFTER CLICK] " + getElementDetails(element));
	}

	// =========================================
	// SEND KEYS EVENTS
	// =========================================

	@Override
	public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
		String details = getElementDetails(element);
		String value = convertCharSequence(keysToSend);
		System.out.println("[BEFORE SEND_KEYS] Element: " + details + " | Value: " + value);
		StepLogger.info("Type into " + details, value);
	}

	@Override
	public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
		System.out.println("[AFTER SEND_KEYS] Element: " + getElementDetails(element) + " | Value: "
				+ convertCharSequence(keysToSend));
	}

	// =========================================
	// CLEAR EVENTS
	// =========================================

	@Override
	public void beforeClear(WebElement element) {
		System.out.println("[BEFORE CLEAR] " + getElementDetails(element));
		StepLogger.info("Clear field", getElementDetails(element));
	}

	@Override
	public void afterClear(WebElement element) {
		System.out.println("[AFTER CLEAR] " + getElementDetails(element));
	}

	// =========================================
	// FIND ELEMENT EVENTS
	// =========================================

	@Override
	public void beforeFindElement(WebDriver driver, By locator) {
		System.out.println("[BEFORE FIND ELEMENT] Locator: " + locator);
	}

	@Override
	public void afterFindElement(WebDriver driver, By locator, WebElement result) {
		System.out.println("[AFTER FIND ELEMENT] Locator: " + locator + " | Found: " + getElementDetails(result));
	}

	// =========================================
	// FIND ELEMENTS EVENTS
	// =========================================

	@Override
	public void beforeFindElements(WebDriver driver, By locator) {
		System.out.println("[BEFORE FIND ELEMENTS] Locator: " + locator);
	}

	@Override
	public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
		System.out.println("[AFTER FIND ELEMENTS] Locator: " + locator + " | Count: " + result.size());
	}

	// =========================================
	// SCRIPT EXECUTION EVENTS
	// =========================================

	@Override
	public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
		System.out.println("[BEFORE JS EXECUTION]");
		System.out.println("Script: " + script);
		System.out.println("Arguments: " + Arrays.toString(args));
		StepLogger.info("Execute JavaScript", script.length() > 120 ? script.substring(0, 120) + "…" : script);
	}

	@Override
	public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
		System.out.println("[AFTER JS EXECUTION]");
		System.out.println("Script: " + script);
		System.out.println("Result: " + result);
	}

	// =========================================
	// HELPER METHODS
	// =========================================

	private String convertCharSequence(CharSequence... keysToSend) {

		StringBuilder builder = new StringBuilder();

		for (CharSequence seq : keysToSend) {
			builder.append(seq);
		}

		return builder.toString();
	}

	private String getElementDetails(WebElement element) {

		try {

			String tagName = safeGet(() -> element.getTagName());
			String id = safeGet(() -> element.getAttribute("id"));
			String name = safeGet(() -> element.getAttribute("name"));
			String className = safeGet(() -> element.getAttribute("class"));
			String text = safeGet(() -> element.getText());

			return String.format("Tag='%s', ID='%s', Name='%s', Class='%s', Text='%s'", tagName, id, name, className,
					text);

		} catch (Exception e) {

			return "Unable to fetch element details. Exception: " + e.getMessage();
		}
	}

	private String safeGet(Supplier<String> supplier) {

		try {

			String value = supplier.get();

			return value == null || value.isBlank() ? "N/A" : value;

		} catch (Exception e) {

			return "N/A";
		}
	}
}