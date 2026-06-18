package com.freecrm.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

	private static ConfigManager instance;

	private final Properties properties;
	private final String environment;

	private ConfigManager() {
		environment = resolveEnvironment();
		properties = new Properties();
		loadProperties();
	}

	public static synchronized ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}

	private void loadProperties() {

		String propFile = environment + ".properties";

		try (InputStream is = getClass().getClassLoader().getResourceAsStream(propFile)) {

			if (is == null) {

				throw new FileNotFoundException(propFile + " " + "Not Found");

			}

			properties.load(is);

		} catch (IOException e) {
			throw new RuntimeException("Failed to load configuration: " + propFile, e);
		}

	}

	private String resolveEnvironment() {

		String env = System.getProperty("env");
		if (env == null || env.isBlank()) {
			env = System.getenv("ENV");
		}

		if (env == null || env.isBlank()) {
			env = "svt";
		}

		return env;
	}

	public String getProperty(String propertyName) {

		return properties.getProperty(propertyName);

	}

}
