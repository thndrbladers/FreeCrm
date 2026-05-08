package com.freecrm.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.freecrm.base.Base;

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
		return homeMenu.isDisplayed();
	}

	public void clickMainMenuItem(String menuItem) {
		getDriver().findElement(By.xpath("//span[text()=" + menuItem + "']")).click();

	}

	public void clickCalender() {
		calenderMenu.click();

	}

	public ContactPage clickContacts() {
		contactsMenu.click();

		return new ContactPage();

	}

}
