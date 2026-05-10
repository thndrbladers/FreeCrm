package com.freecrm.reports;

import com.aventstack.extentreports.ExtentTest;

/**
 * ThreadLocal holder so every parallel test thread gets its own ExtentTest node.
 */
public class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> threadLocalTest = new ThreadLocal<>();

    private ExtentTestManager() {
    }

    /** Create a new ExtentTest node and store it in the current thread. */
    public static synchronized ExtentTest startTest(String testName, String description) {
        ExtentTest test = ExtentReportManager.getInstance()
                .createTest(testName, description);
        threadLocalTest.set(test);
        return test;
    }

    /** Returns the ExtentTest for the current thread. */
    public static ExtentTest getTest() {
        return threadLocalTest.get();
    }

    /** Cleans up the ThreadLocal to prevent memory leaks. */
    public static void endTest() {
        threadLocalTest.remove();
    }
}
