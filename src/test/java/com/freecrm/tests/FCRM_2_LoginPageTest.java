package com.freecrm.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.freecrm.base.Base;
import com.freecrm.pages.LoginPage;
import com.freecrm.reports.ExtentSoftAssert;
import com.freecrm.reports.StepLogger;
import com.freecrm.utility.ExcelReaderUtility;

public class FCRM_2_LoginPageTest extends Base {

	LoginPage loginPage;

	@BeforeMethod
	public void setUp() {
		initialization();
		loginPage = new LoginPage();

	}

	@Test(description = "Test to verify that user cannot login with invalid credentials", groups = {
			"regression" }, dataProvider = "negativeLoginData")
	public void negative_login_test(String email, String password) {
		ExtentSoftAssert sa = new ExtentSoftAssert();
		StepLogger.info("Attempting to login with email: " + email + " and password: " + password);
		loginPage.login(email, password);
		sa.assertTrue(loginPage.isInvalidLoginErrorDisplayed(),
				"Invalid login error message should be displayed for email: " + email + " and password: " + password);

		sa.assertAll();
	}

	@AfterMethod
	public void tearDown() {
		quitDriver();
	}

	@DataProvider(name = "negativeLoginData")
	public Object[][] getTestData() {
		ExcelReaderUtility td = new ExcelReaderUtility("negative_login_testdata", "negative_login_testdata.xlsx");
		return td.getExcelData();

	}
}
