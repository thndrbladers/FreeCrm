package com.freecrm.base;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.freecrm.listeners.FreeCrmListener;

public class Base {

	private final static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
	private final static ThreadLocal<WebDriverWait> threadLocalWait = new ThreadLocal<>();

	/**
	 * Loaded once by the first Base instance; safe to read from any thread after
	 * that.
	 */
	private static final AtomicReference<Properties> staticConfig = new AtomicReference<>();

	private final Properties property;
	private final String environment;

	public Base() {
		this.property = new Properties();
		environment = resolveEnvironment();
		loadProperties();
	}

	public void initialization() {

		WebDriver localDriver;

		String browser = getProperty("browser");
		boolean isHeadless = getProperty("headless").equalsIgnoreCase("true");
		ChromeOptions chromeOptions;

		switch (browser.toLowerCase()) {

		case "chrome":
			chromeOptions = new ChromeOptions();
			if (isHeadless) {
				chromeOptions.addArguments("--headless=new");
			}
			localDriver = new ChromeDriver(chromeOptions);
			break;

		case "edge":
			localDriver = new EdgeDriver();
			break;

		case "firefox":
			localDriver = new FirefoxDriver();
			break;

		case "safari":
			localDriver = new SafariDriver();
			break;

		default:
			chromeOptions = new ChromeOptions();
			System.out.println("Selecting default browser - Chrome : ");
			if (isHeadless) {
				chromeOptions.addArguments("--headless=new");
			}
			localDriver = new ChromeDriver(chromeOptions);
			break;
		}

		localDriver.manage().window().maximize();
		localDriver.manage().timeouts()
				.pageLoadTimeout(Duration.ofSeconds(Integer.valueOf(getProperty("pageloadTimeout"))));
		localDriver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Integer.valueOf(getProperty("implicitWait"))));
		localDriver.get(getProperty("url"));

		threadLocalWait
				.set(new WebDriverWait(localDriver, Duration.ofSeconds(Integer.valueOf(getProperty("explicitWait")))));

		WebDriverListener eventListener = new FreeCrmListener();
		WebDriver decoratedDriver = new EventFiringDecorator<>(eventListener).decorate(localDriver);
		threadLocalDriver.set(decoratedDriver);

	}

	public static WebDriver getDriver() {
		return threadLocalDriver.get();
	}

	public static WebDriverWait getWait() {
		return threadLocalWait.get();
	}

	private String resolveEnvironment() {
		String env = System.getProperty("env");// 1. -Denv system property (e.g., mvn test -Denv=dev)
		if (env == null || env.isBlank()) {
			env = System.getenv("ENV");// 2. ENV OS environment variable (e.g., set in Jenkins/GitHub Actions)
		}
		if (env == null || env.isBlank()) {
			env = "svt";
		}

		return env;
	}

	private void loadProperties() {

		String file = environment + ".properties";

		try (InputStream is = getClass().getClassLoader().getResourceAsStream(file)) {

			if (is == null) {

				throw new RuntimeException("Configuration file not found: " + file);
			}

			property.load(is);

			// Publish to static config exactly once (first thread wins; all threads share
			// the same env)
			staticConfig.compareAndSet(null, property);

		} catch (IOException e) {
			throw new RuntimeException("Failed to load configuration: " + file, e);
		}

	}

	public String getProperty(String key) {
		return property.getProperty(key);
	}

	/**
	 * Static accessor for utilities (e.g. StepLogger) that need config values
	 * without holding a Base instance. Thread-safe: reads from an immutable
	 * AtomicReference after the first Base is constructed.
	 *
	 * @return the property value, or {@code null} if config is not yet loaded or
	 *         key is absent.
	 */
	public static String getConfig(String key) {
		Properties cfg = staticConfig.get();
		return cfg != null ? cfg.getProperty(key) : null;
	}

	public void quitDriver() {
		if (threadLocalDriver.get() != null) {
			threadLocalDriver.get().quit();
			threadLocalDriver.remove();
			threadLocalWait.remove();
		}
	}

}