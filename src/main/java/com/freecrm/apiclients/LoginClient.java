package com.freecrm.apiclients;

import io.restassured.response.Response;

public class LoginClient {

	private static final String AUTH_ENDPOINT = "/1/auth/";

	private ApiClient apiClient;

	public LoginClient() {
		apiClient = new ApiClient();
	}

	public String getAuthEndpoint() {
		return AUTH_ENDPOINT;
	}

	public Response getAuthRawResponse(Object body) {

		return apiClient.post(AUTH_ENDPOINT, body);
	}

}
