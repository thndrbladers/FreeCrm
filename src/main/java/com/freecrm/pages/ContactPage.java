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

import com.freecrm.base.Base;
import com.freecrm.utility.ExcelUtility;

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

	public ContactPage() {
		PageFactory.initElements(getDriver(), this);
	}

	public void clickInputSearchOption(String section, String searchOption) {

		getDriver().findElement(By.xpath("//label[text()='" + section
				+ "']/parent::div//i/following-sibling::span[text()='" + searchOption + "']")).click();
	}

	public void fillAndSaveContactForms(String sheetName, String fileName) {
		ExcelUtility excelUtility = new ExcelUtility(sheetName, fileName);
		List<Object[]> list = excelUtility.getExcelDataAsList();

		for (Object[] o : list) {
			createButton.click();
			firstNameInput.sendKeys(o[0].toString());
			lastNameInput.sendKeys(o[1].toString());
			emailInput.sendKeys(o[2].toString());
			streetAddressInput.sendKeys(o[3].toString());
			cityInput.sendKeys(o[4].toString());
			stateInput.sendKeys(o[5].toString());
			postCodeInput.sendKeys(o[6].toString());

			countrySearchInput.clear();
			countrySearchInput.sendKeys(o[7].toString());
			clickInputSearchOption("Address", o[7].toString());

			phoneNumberCountryCodeSearchInput.clear();
			phoneNumberCountryCodeSearchInput.sendKeys(o[9].toString());
			clickInputSearchOption("Phone", o[7].toString());

			phoneNumberInput.sendKeys(o[8].toString());
			saveButton.click();
			wait.until(ExpectedConditions.elementToBeClickable(getDriver().findElement(By
					.xpath("//span[@class='selectable '][text()='" + o[0].toString() + " " + o[1].toString() + "']"))));

			new HomePage().clickContacts();

		}

	}

	public String contactPageTitle() {
		return getDriver().getTitle();
	}

	public String contactPageUrl() {
		return getDriver().getCurrentUrl();
	}

}
