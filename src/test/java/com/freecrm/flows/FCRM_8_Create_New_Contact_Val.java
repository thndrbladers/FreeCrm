package com.freecrm.flows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.freecrm.base.Base;
import com.freecrm.pages.ContactPage;
import com.freecrm.pages.HomePage;
import com.freecrm.pages.LoginPage;
import com.freecrm.utility.ExcelReaderUtility;
import com.freecrm.utility.ExcelWriterUtility;

public class FCRM_8_Create_New_Contact_Val extends Base {

	LoginPage loginPage;
	HomePage homePage;
	ContactPage contactPage;

	@BeforeMethod
	// @BeforeClass
	public void setUp() {
		initialization();
		this.loginPage = new LoginPage();
		this.homePage = loginPage.login("freecrm.rdg@mailinator.com", "Freecrm.rdg@123");
		this.contactPage = homePage.clickContacts();

	}

	@Test(description = "Test to create new contact with valid data", groups = { "smoke", "regression" })

	// dataProvider = "getTestData"
	public void create_new_contact_val() {
		SoftAssert sa = new SoftAssert();

		sa.assertTrue(homePage.isHomeMenuDisplayed());
		sa.assertEquals(contactPage.contactPageTitle(), "Free CRM");
		sa.assertEquals(contactPage.contactPageUrl(), "https://ui.freecrm.com/contacts");

		ExcelReaderUtility td = new ExcelReaderUtility("TestData", "create_contacts_testdata.xlsx");
		List<Object[]> list = td.getExcelDataAsList();

		for (Object[] oArray : list) {

			contactPage.clickCreate();

			contactPage.fillContactForm(oArray[0].toString(), // firstName
					oArray[1].toString(), // lastName
					oArray[2].toString(), // email
					oArray[3].toString(), // street
					oArray[4].toString(), // city
					oArray[5].toString(), // state
					oArray[6].toString(), // postCode
					oArray[7].toString(), // country
					oArray[8].toString(), // phone
					oArray[9].toString() // countryCode

			);

			contactPage.saveContact(oArray[0].toString(), oArray[1].toString());

			this.contactPage = contactPage.clickContacts();

		}

		ExcelWriterUtility ew = new ExcelWriterUtility();

		List<Object[]> webTable = ew.writeTableInExcel(getDriver());

		for (Object[] expected : list) {

			String expectedName = expected[0].toString() + " " + expected[1].toString();

			String expectedAddress = expected[3].toString() + ", " + expected[4].toString() + ", "
					+ expected[5].toString() + ", " + expected[6].toString() + ", " + expected[7].toString();

			String expectedPhone = expected[8].toString();

			String expectedEmail = expected[2].toString();

			boolean flag = false;
			for (Object[] actual : webTable) {

				String actualName = actual[1].toString();
				String actualAddress = actual[2].toString();
				String actualPhone = actual[5].toString();
				String actualEmail = actual[6].toString();

				if (actualName.equals(expectedName)) {
					flag = true;
					// Validate contact name
					sa.assertEquals(actualName, expectedName, "Contact name mismatch");

					// Validate contact address
					sa.assertEquals(actualAddress, expectedAddress, "Contact address mismatch");

					// Validate phone number (UI includes country code with phone)
					sa.assertTrue(actualPhone.contains(expectedPhone), "Contact phone mismatch");

					// Validate email address
					sa.assertEquals(actualEmail, expectedEmail, "Contact email mismatch");
					break;

				}

			}
			if (!flag) {

				sa.fail("Data row not found for : " + expectedName);
			}

		}

		sa.assertAll();
	}

	@Test(description = "Delete freshly created test data at once", dependsOnMethods = "create_new_contact_val", groups = {
			"smoke", "regression" })

	public void delete_all_contacts_val() {

		SoftAssert sa = new SoftAssert();

		contactPage.selectAllContacts();
		contactPage.selectAction("Delete");
		contactPage.clickCheckmark("Yes");

		sa.assertTrue(contactPage.isNoContactsFoundMessageDisplayed(), "Contacts were not deleted successfully.");

		sa.assertAll();

	}

	@DataProvider
	public Iterator<Object[]> getTestData() {

		ExcelReaderUtility td = new ExcelReaderUtility("TestData", "create_contacts_testdata.xlsx");
		List<Object[]> list = td.getExcelDataAsList();

		return list.iterator();
	}

	@AfterMethod
	// @AfterClass
	public void tearDown() {
		quitDriver();

	}

}
