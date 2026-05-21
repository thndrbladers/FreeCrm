package com.freecrm.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.freecrm.base.Base;
import com.freecrm.reports.StepLogger;

public class CompaniesPage extends Base {

	public CompaniesPage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(xpath = "//button[text()='Create']")
	WebElement createButton;

	@FindBy(name = "name")
	WebElement companyName;

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

	@FindBy(xpath = "//button[text()='Save']")
	WebElement saveButton;

	@FindBy(xpath = "//table/tbody/tr")
	List<WebElement> tableRows;

	@FindBy(xpath = "//table/tbody/tr/td[position()>1 and position()<last()]")
	List<WebElement> tableTextColumns;

	@FindBy(xpath = "//table/tbody/tr[1]/td[position()>1 and position()<last()]")
	List<WebElement> tableTextColumnsFirstRow;
	
	@FindBy(xpath = "//div[@class='ui warning message']/p[text()='No records found']")
	WebElement noRecordsFoundMessage;

	private By tableXpath = By.xpath("//table/tbody/");

	/** Clicks the Create button to open the new Company form */
	public void clickCreate() {
		StepLogger.info("Clicking Create button to open new Company form");
		createButton.click();
	}

	public WebElement getNoRecordsFoundMessage() {
		return noRecordsFoundMessage;
	}

	public void clickInputSearchOption(String section, String searchOption) {
		StepLogger.info("Selecting '" + searchOption + "' from '" + section + "' search options");

		getDriver().findElement(By.xpath("//label[text()='" + section
				+ "']/parent::div//i/following-sibling::span[text()='" + searchOption + "']")).click();
	}

	/**
	 * Clicks Save and waits for the new Company to appear in the Company edit page
	 */
	public void saveCompany(String name) {
		StepLogger.info("Saving Company and waiting for it to appear in the Company edit page - Name: " + name);

		saveButton.click();

		By contactName = By.xpath("//span[@class='selectable '][text()='" + name + "']");

		getWait().until(ExpectedConditions.elementToBeClickable(contactName));
	}

	private By dynamicTableRowColCellXpath(int row, int column) {

		return By.xpath("//table/tbody/tr[" + row + "]/td[position()>1 and position()<last()][" + column + "]");

	}

	public List<Object[]> getCompaniesTextTableData() {

		int noOfRows = tableRows.size();
		int noOfCols = tableTextColumnsFirstRow.size();

		System.out.println("Number of rows: " + noOfRows);
		System.out.println("Number of columns: " + noOfCols);
		List<Object[]> list = new ArrayList<>();

		Object[] temp;

		for (int i = 0; i < noOfRows; i++) {
			temp = new String[noOfCols];

			for (int j = 0; j < noOfCols; j++) {

				temp[j] = getDriver().findElement(dynamicTableRowColCellXpath(i + 1, j + 1)).getText();

			}

			list.add(temp);

		}

		return list;

	}

	/** Fills all form fields for a single Company */
	public void fillCompaniesForm(String name, String street, String city, String state, String postCode,
			String country) {
		companyName.sendKeys(name);
		streetAddressInput.sendKeys(street);
		cityInput.sendKeys(city);
		stateInput.sendKeys(state);
		postCodeInput.sendKeys(postCode);
		countrySearchInput.clear();
		countrySearchInput.sendKeys(country);
		clickInputSearchOption("Address", country);
	}

}
