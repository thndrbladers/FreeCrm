package com.freecrm.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.Status;
import com.freecrm.base.Base;
import com.freecrm.reports.StepLogger;
import com.freecrm.utility.ScreenshotUtility;

public class HomePage extends Base {

	@FindBy(xpath = "//span[text()='Home']")
	WebElement homeMenu;

	@FindBy(xpath = "//span[text()='Calender']")
	WebElement calenderMenu;

	@FindBy(xpath = "//span[text()='Contacts']")
	WebElement contactsMenu;

	@FindBy(xpath = "//div[@id='main-nav']//a/i/following-sibling::span")
	List<WebElement> mainMenuItems;

	public HomePage() {
		PageFactory.initElements(getDriver(), this);
	}

	public boolean isHomeMenuDisplayed() {
		StepLogger.info("Checking if Home menu is displayed");
		return homeMenu.isDisplayed();
	}

	public void clickMainMenuItem(String menuItem) {
		StepLogger.info("Clicking on main menu item: " + menuItem);
		getDriver().findElement(By.xpath("//span[text()=" + menuItem + "']")).click();

	}

	public void clickCalender() {
		StepLogger.info("Clicking on Calender menu");
		calenderMenu.click();

	}

	public ContactPage clickContacts() {
		StepLogger.info("Clicking on Contacts menu");
		contactsMenu.click();
		// To collapse the user menu if it's open and blocking the view of the contacts
		// page
		getDriver().findElement(By.xpath("//span[@class='user-display']")).click();
		getWait().until(ExpectedConditions
				.invisibilityOf(getDriver().findElement(By.xpath("//div[contains(@class,'inline loader')]"))));
		getWait().until(ExpectedConditions
				.invisibilityOf(getDriver().findElement(By.xpath("//div[contains(@class,'inline loader')]"))));

		return new ContactPage();

	}

}
