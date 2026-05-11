package com.freecrm.pages;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;
import com.freecrm.base.Base;
import com.freecrm.reports.StepLogger;
import com.freecrm.utility.ExcelReaderUtility;
import com.freecrm.utility.ScreenshotUtility;

public class ContactPage extends Base {

	HomePage homePage;

	@FindBy(xpath = "//button[text()='Create']")
	WebElement createButton;

	@FindBy(name = "first_name")
	WebElement firstNameInput;

	@FindBy(name = "last_name")
	WebElement lastNameInput;

	@FindBy(xpath = "//input[@placeholder='Email address']")
	WebElement emailInput;

	// Street Address
	@FindBy(xpath = "//input[@placeholder='Street Address']")
	WebElement streetAddressInput;

	@FindBy(xpath = "//input[@placeholder='City']")
	WebElement cityInput;

	@FindBy(xpath = "//input[@placeholder='State / County']")
	WebElement stateInput;

	@FindBy(xpath = "//input[@placeholder='Post Code']")
	WebElement postCodeInput;

	@FindBy(xpath = "//label[text()='Address']/parent::div//input[@class='search']")
	WebElement countrySearchInput;

	@FindBy(xpath = "//input[@placeholder='Number']")
	WebElement phoneNumberInput;

	@FindBy(xpath = "//label[text()='Phone']/parent::div//input[@class='search']")
	WebElement phoneNumberCountryCodeSearchInput;

	@FindBy(xpath = "//button[text()='Save']")
	WebElement saveButton;

	@FindBy(xpath = "//span[text()='Contacts']")
	WebElement contactsMenu;

	@FindBy(xpath = "//div[@title='Select All']")
	WebElement selectAllCheckbox;

	@FindBy(xpath = "//div[@name='action' and @role='listbox']")
	WebElement actionDropdown;

	@FindBy(xpath = "(//div[@role='button']/i[@class='checkmark icon'])[1]")
	WebElement checkmarkButton;

	@FindBy(xpath = "//div[@class='ui warning message']/p[text()='No records found']")
	WebElement noRecordsFoundMessage;

	public ContactPage() {
		PageFactory.initElements(getDriver(), this);
	}

	public void clickInputSearchOption(String section, String searchOption) {
		StepLogger.info(
				"Selecting '" + searchOption + "' from '" + section + "' search options");

		getDriver().findElement(By.xpath("//label[text()='" + section
				+ "']/parent::div//i/following-sibling::span[text()='" + searchOption + "']")).click();
	}

	public void selectAction(String action) {
		StepLogger.info( "Selecting '" + action + "' from action dropdown");
		actionDropdown.click();
		getDriver().findElement(By.xpath("(//div[@role='option']/span[text()='" + action + "'])[1]")).click();
	}

	public void clickCheckmark(String confirmation) {
		StepLogger.info(
				"Clicking checkmark button and confirming with '" + confirmation + "'");
		checkmarkButton.click();
		getDriver().findElement(By.xpath("//div[@class='actions']/button[text()='" + confirmation + "']")).click();
	}

	/** Clicks the Create button to open the new contact form */
	public void clickCreate() {
		StepLogger.info( "Clicking Create button to open new contact form");
		createButton.click();
	}

	public void selectAllContacts() {
		StepLogger.info( "Selecting all contacts using the select all checkbox");
		selectAllCheckbox.click();
	}

	public boolean isNoContactsFoundMessageDisplayed() {
		StepLogger.info( "Checking if 'No records found' message is displayed");
		return noRecordsFoundMessage.isDisplayed();
	}

	/** Fills all form fields for a single contact */
	public void fillContactForm(String firstName, String lastName, String email, String street, String city,
			String state, String postCode, String country, String phone, String countryCode) {
		StepLogger.info(
				"Filling contact form with provided details - First Name: " + firstName + ", Last Name: " + lastName
						+ ", Email: " + email + ", Street: " + street + ", City: " + city + ", State: " + state
						+ ", Post Code: " + postCode + ", Country: " + country + ", Phone: " + phone
						+ ", Country Code: " + countryCode);
		firstNameInput.sendKeys(firstName);
		lastNameInput.sendKeys(lastName);
		emailInput.sendKeys(email);
		streetAddressInput.sendKeys(street);
		cityInput.sendKeys(city);
		stateInput.sendKeys(state);
		postCodeInput.sendKeys(postCode);
		countrySearchInput.clear();
		countrySearchInput.sendKeys(country);
		clickInputSearchOption("Address", country);
		phoneNumberCountryCodeSearchInput.clear();
		phoneNumberCountryCodeSearchInput.sendKeys(countryCode);
		clickInputSearchOption("Phone", countryCode);
		phoneNumberInput.sendKeys(phone);
	}

	/** Clicks Save and waits for the new contact to appear in the list */
	public void saveContact(String firstName, String lastName) {
		StepLogger.info(
				"Saving contact and waiting for it to appear in the contact list - First Name: " + firstName
						+ ", Last Name: " + lastName);

		saveButton.click();

		By contactName = By.xpath("//span[@class='selectable '][text()='" + firstName + " " + lastName + "']");

		getWait().until(ExpectedConditions.elementToBeClickable(contactName));
	}

	public String contactPageTitle() {

		StepLogger.info( "Getting contact page title");
		return getDriver().getTitle();
	}

	public ContactPage clickContacts() {
		StepLogger.info("Clicking on Contacts menu to navigate to contact page");

		contactsMenu.click();
		// To collapse the user menu if it's open and blocking the view of the contacts
		// page
		getDriver().findElement(By.xpath("//span[@class='user-display']")).click();

		return new ContactPage();

	}

	public String contactPageUrl() {
		StepLogger.info( "Getting contact page URL");
		return getDriver().getCurrentUrl();
	}
}