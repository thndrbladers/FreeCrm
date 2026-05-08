package com.freecrm.flows;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.freecrm.base.Base;
import com.freecrm.pages.ContactPage;
import com.freecrm.pages.HomePage;
import com.freecrm.pages.LoginPage;

public class FCRM_8_Create_New_Contact_Val extends Base {

	LoginPage loginPage;
	HomePage homePage;
	ContactPage contactPage;

	@BeforeMethod
	public void setUp() {
		initialization();
		this.loginPage = new LoginPage();
		this.homePage = loginPage.login("freecrm.rdg@mailinator.com", "Freecrm.rdg@123");
		this.contactPage = homePage.clickContacts();

	}

	@Test
	public void create_New_Contact_Val() {

		SoftAssert sa = new SoftAssert();

		sa.assertEquals(contactPage.contactPageTitle(), "Free CRM");
		sa.assertEquals(contactPage.contactPageUrl(), "https://ui.freecrm.com/contacts");

		contactPage.fillAndSaveContactForms("TestData",
				"C:\\Users\\thndr\\Desktop\\Mission2026_QAAutomationProjects\\FreeCrm\\src\\main\\java\\com\\freecrm\\testdata\\selenium_test_data_10_rows.xlsx");

		sa.assertAll();
	}

	@AfterMethod
	public void tearDown() {
		quitDriver();

	}

}
