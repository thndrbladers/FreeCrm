package com.freecrm.utility;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.freecrm.base.Base;

/**
 * Captures screenshots and returns them as a Base64-encoded string
 * so they can be embedded directly in the ExtentReports HTML report.
 */
public class ScreenshotUtility {

    private ScreenshotUtility() {
    }

    /**
     * Takes a screenshot of the current browser window.
     *
     * @return Base64-encoded PNG string, or {@code null} if the driver is unavailable.
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
}
