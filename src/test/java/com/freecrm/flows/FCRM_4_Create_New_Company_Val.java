package com.freecrm.flows;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.freecrm.base.Base;
import com.freecrm.pages.CompaniesPage;
import com.freecrm.pages.HomePage;
import com.freecrm.pages.LoginPage;
import com.freecrm.reports.ExtentSoftAssert;
import com.freecrm.utility.DatabaseUtil;
import com.freecrm.utility.ExcelReaderUtility;
import com.freecrm.utility.ExcelWriterUtility;

public class FCRM_4_Create_New_Company_Val extends Base {

	HomePage homePage;
	LoginPage loginPage;
	CompaniesPage companiesPage;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		initialization();
		this.loginPage = new LoginPage();
		this.homePage = loginPage.login(Base.getConfig("username"), Base.getConfig("password"));
		this.companiesPage = homePage.clickCompanies();
	}

	@Test
	// dataProvider = "dbData"
	/*
	 * String name, String streetAddress, String city, String stateCounty, String
	 * postcode, String country
	 */
	public void create_new_comapny_val() {

		List<Object[]> temp = DatabaseUtil
				.getQueryData("Select NAME,STREET_ADDRESS,CITY,STATE_COUNTY,POST_CODE,COUNTRY from companies");

		for (Object[] o : temp) {

			companiesPage.clickCreate();

			companiesPage.fillCompaniesForm(o[0].toString(), o[1].toString(), o[2].toString(), o[3].toString(),
					o[4].toString(), o[5].toString());

			companiesPage.saveCompany(o[0].toString());

			homePage.clickCompanies();

		}

	}

	@Test
	public void companies_tabularData_Val() {
		ExtentSoftAssert sa = new ExtentSoftAssert();

		getWait().until(ExpectedConditions.invisibilityOfAllElements(companiesPage.getNoRecordsFoundMessage()));

		getDriver().findElement(By.xpath("//th[text()='Name']")).click();
		getDriver().findElement(By.xpath("//th[text()='Name']")).click();

		getWait().until(ExpectedConditions.invisibilityOfAllElements(companiesPage.getNoRecordsFoundMessage()));

		List<Object[]> actualList = companiesPage.getCompaniesTextTableData();
		List<Object[]> expectList = DatabaseUtil.getQueryData("SELECT \r\n" + "    NAME,\r\n"
				+ "    CONCAT(STREET_ADDRESS, ', ', CITY, ', ', STATE_COUNTY, ', ', POST_CODE) AS Address\r\n"
				+ "FROM companies\r\n" + "ORDER BY NAME ASC\r\n" + "FETCH FIRST 10 ROWS ONLY");

		for (int i = 0; i < expectList.size(); i++) {
			Object[] ac = actualList.get(i);
			Object[] ep = expectList.get(i);

			for (int j = 0; j < ac.length; j++) {
				System.out.println("Actual: " + ac[j].toString() + " | Expected: " + ep[j].toString());
				sa.assertEquals(ac[j].toString(), ep[j].toString());
			}

		}
		sa.assertAll();
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		quitDriver();

	}

	@DataProvider(name = "dbData")
	public Iterator<Object[]> getData() {

		List<Object[]> temp = DatabaseUtil
				.getQueryData("Select NAME,STREET_ADDRESS,CITY,STATE_COUNTY,POST_CODE,COUNTRY from companies");

		return temp.iterator();

	}

}
