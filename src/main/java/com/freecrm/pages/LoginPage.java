package com.freecrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.freecrm.base.Base;

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

		emailInput.sendKeys(email);
		passwordInput.sendKeys(password);
		loginButton.click();

		return new HomePage();
	}

	public boolean isInvalidLoginErrorDisplayed() {

		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(invalidLoginErrorLocator));

		return error.isDisplayed();
	}

	public void clickSignUp() {
		signUpLink.click();
	}

}
