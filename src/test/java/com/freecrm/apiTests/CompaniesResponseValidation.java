package com.freecrm.apiTests;

import java.io.File;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.freecrm.apiclients.ApiClient;
import com.freecrm.apiclients.CompaniesClient;
import com.freecrm.pojo.Company;
import com.freecrm.pojo.RootResponse;

import io.restassured.response.Response;
import tools.jackson.databind.ObjectMapper;

public class CompaniesResponseValidation {

	private ApiClient apiClient;
	private CompaniesClient companiesClient;

	@BeforeMethod
	public void setUp() {
		companiesClient = new CompaniesClient();

	}

	@Test
	public void companiesResponseValidation() {

		ObjectMapper om = new ObjectMapper();
		RootResponse expectedResponse = om.readValue(new File("src/main/java/com/freecrm/testdata/CompaniesData.json"),
				RootResponse.class);

		RootResponse actualResponse = companiesClient.getCompaniesResponse().as(RootResponse.class);

		System.out.println(actualResponse.getProcessTime());

		System.out.println(expectedResponse);

		System.out.println(actualResponse);

		Assert.assertEquals(expectedResponse, actualResponse);
	}
}
