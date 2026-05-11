package com.freecrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.Status;
import com.freecrm.base.Base;
import com.freecrm.reports.StepLogger;
//import com.freecrm.utility.ScreenshotUtility;
import com.freecrm.utility.ScreenshotUtility;

public class LoginPage extends Base {

	@FindBy(name = "email")
	WebElement emailInput;

	@FindBy(name = "password")
	WebElement passwordInput;

	@FindBy(xpath = "//div[text()='Login']")
	WebElement loginButton;

	@FindBy(xpath = "//a[text()='Sign Up']")
	WebElement signUpLink;

	private By invalidLoginErrorLocator = By
			.xpath("//div[text()='Something went wrong...']/following-sibling::p[contains(text(),'Invalid')]");

	public LoginPage() {
		PageFactory.initElements(getDriver(), this);
	}

	public HomePage login(String email, String password) {
		StepLogger.info("Attempting to log in with email: " + email + ", password : " + password);
		ScreenshotUtility.takeScreenshot();

		emailInput.sendKeys(email);
		passwordInput.sendKeys(password);

		loginButton.click();
		return new HomePage();
	}

	public String getPageTitle() {
		StepLogger.info("Getting page title");
		return getDriver().getTitle();
	}

	public boolean isInvalidLoginErrorDisplayed() {
		StepLogger.info("Checking for invalid login error message");

		WebElement error = getWait().until(ExpectedConditions.visibilityOfElementLocated(invalidLoginErrorLocator));

		return error.isDisplayed();
	}

	public void clickSignUp() {
		StepLogger.info("Clicking on Sign Up link");
		signUpLink.click();
	}

}
