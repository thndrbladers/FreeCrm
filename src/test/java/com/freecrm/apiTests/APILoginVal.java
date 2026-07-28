package com.freecrm.apiTests;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.freecrm.apiclients.ApiClient;
import com.freecrm.apiclients.LoginClient;
import com.freecrm.pojo.LoginRequest;
import com.freecrm.utility.ExcelReaderUtility;

public class APILoginVal {

	public ApiClient apiClient;

	public LoginClient loginClient;

	private static final Logger LOG = LogManager.getLogger(APILoginVal.class);

	@BeforeMethod
	public void setUp() {

		loginClient = new LoginClient();

	}

	@Test(dataProvider = "loginData")
	public void apiLoginVal(String email, String password, String expectedStatusCode) {

		LOG.info("Login data: email={}, password={}, expectedStatusCode={}", email, password, expectedStatusCode);

		LoginRequest lr = new LoginRequest();

		lr.setEmail(email);
		lr.setPassword(password);

		Assert.assertEquals(expectedStatusCode, String.valueOf(loginClient.getAuthRawResponse(lr).getStatusCode()));
	}

	@DataProvider(name = "loginData")
	public Iterator<Object[]> loginData() {

		ExcelReaderUtility eru = new ExcelReaderUtility("td", "api_login_testdata.xlsx");

		List<Object[]> logindata = eru.getExcelDataAsList();

		return logindata.iterator();
	}
}
