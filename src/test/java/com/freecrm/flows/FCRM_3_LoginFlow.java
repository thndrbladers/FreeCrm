package com.freecrm.flows;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.freecrm.base.Base;
import com.freecrm.pages.HomePage;
import com.freecrm.pages.LoginPage;
import com.freecrm.reports.ExtentSoftAssert;
import com.freecrm.reports.StepLogger;
import com.freecrm.utility.ExcelReaderUtility;

public class FCRM_3_LoginFlow extends Base {

	LoginPage loginPage;
	HomePage homePage;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		initialization();
		loginPage = new LoginPage();

	}

	@Test(description = "Verify that user can login with valid credentials", groups = { "smoke", "FCRM_3",
			"Release_1" })
	public void verify_valid_login() {
		ExtentSoftAssert sa = new ExtentSoftAssert();
		this.homePage = loginPage.login(Base.getConfig("username"), Base.getConfig("password"));
		sa.assertTrue(homePage.isHomeMenuDisplayed(), "Home menu should be displayed after successful login");
		StepLogger.info("Login successful, Home menu is displayed");
		sa.assertAll();
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		quitDriver();
	}
}
