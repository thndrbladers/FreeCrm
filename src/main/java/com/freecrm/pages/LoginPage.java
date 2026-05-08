package com.freecrm.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
	
	public LoginPage() {
		PageFactory.initElements(getDriver(), this);
	}

	public HomePage login(String email, String password) {

		System.out.println("Entered here");

		emailInput.sendKeys(email);
		passwordInput.sendKeys(password);
		loginButton.click();
		System.out.println("Entered here");

		return new HomePage();
	}

	public void clickSignUp() {
		signUpLink.click();
	}

}
