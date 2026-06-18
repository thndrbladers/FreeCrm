package com.freecrm.apiclients;

import io.restassured.response.Response;

public class CompaniesClient {

	private static final String COMPANIES_ENDPOINT = "/1/companies/?start=0&filter=null&sort=&export=false";

	private ApiClient apiClient;

	public CompaniesClient() {
		apiClient = new ApiClient();
		apiClient.withDefaultAuth();
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public String getCompaniesEndpoint() {
		return COMPANIES_ENDPOINT;
	}

	public Response getCompaniesResponse() {

		return apiClient.get(COMPANIES_ENDPOINT);

	}

}
