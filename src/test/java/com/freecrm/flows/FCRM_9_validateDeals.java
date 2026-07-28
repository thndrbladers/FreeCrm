package com.freecrm.flows;

import org.testng.annotations.Test;

import com.freecrm.base.Base;
import com.freecrm.pages.DealsPage;
import com.freecrm.pages.HomePage;
import com.freecrm.pages.LoginPage;
import com.freecrm.reports.ExtentSoftAssert;
import com.freecrm.utility.DatabaseUtil;

import org.testng.annotations.BeforeMethod;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;

public class FCRM_9_validateDeals extends Base {

	LoginPage loginPage;
	HomePage homePage;
	DealsPage dealsPage;

	private static Logger LOG = LogManager.getLogger(FCRM_9_validateDeals.class);

	@BeforeMethod(alwaysRun = true)
	public void setUp() {

		initialization();

		loginPage = new LoginPage();

		LOG.info("Usernsme : {}, Password : {} ", getProperty("username"), getProperty("password"));

		homePage = loginPage.login(getProperty("username"), getProperty("password"));
		dealsPage = homePage.clickDealsMenu();

	}

	@Test
	public void validateDealsTableData() {

		LOG.info("TableData : {}", dealsPage.getDealsTabularData());

		List<Object[]> actual = dealsPage.getDealsTabularData();

		List<Object[]> expected = DatabaseUtil.getQueryData("SELECT\n" + "    TITLE,\n" + "    COMPANY,\n"
				+ "    TO_CHAR(CLOSE_DATE, 'DD/MM/YYYY HH24:MI') AS CLOSE_DATE,\n" + "    AMOUNT,\n" + "    STATUS,\n"
				+ "    STAGE\n" + "FROM deals");

		for (int i = 0; i < expected.size(); i++) {
			LOG.info("Expected: {}", Arrays.toString(expected.get(i)));
			LOG.info("Actual  : {}", Arrays.toString(actual.get(i)));

			Assert.assertEquals(actual.get(i), expected.get(i), "Mismatch at row " + (i + 1));
		}

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {

		quitDriver();
	}

}
