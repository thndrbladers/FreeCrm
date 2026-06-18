package com.freecrm.apiclients;

import java.util.Map;

import com.freecrm.base.ConfigManager;
import com.freecrm.pojo.LoginRequest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiClient {

	private final RequestSpecification requestSpec;

	public ApiClient() {
		ConfigManager configManager = ConfigManager.getInstance();

		requestSpec = new RequestSpecBuilder().setBaseUri(configManager.getProperty("baseUrl"))
				.setContentType(ContentType.JSON).build();

	}

	public RequestSpecification given() {
		RequestSpecification spec = RestAssured.given().spec(requestSpec).log().all();
		return spec;
	}

	public Response get(String endpoint) {

		return given().when().get(endpoint);
	}

	public Response post(String endpoint) {

		return given().when().post(endpoint);
	}

	public Response post(String endpoint, Object body) {

		return given().body(body).when().post(endpoint);
	}

	public Response post(String endpoint, Map<String, ?> body, Map<String, String> headers) {

		return given().headers(headers).body(body).post(endpoint);
	}

	public Response put(String endpoint, Object put) {

		return given().body(put).when().put(endpoint);
	}

	public Response put(String endpoint, Map<String, ?> body, Map<String, String> headers) {

		return given().headers(headers).body(body).put(endpoint);
	}

	public Response delete(String endpoint, String path) {

		return given().pathParam("path", path).when().delete(endpoint + "/{path}");
	}

	public ApiClient withDefaultAuth() {
		ConfigManager config = ConfigManager.getInstance();

		LoginRequest lr = new LoginRequest();
		lr.setEmail(config.getInstance().getProperty("username"));
		lr.setPassword(config.getInstance().getProperty("password"));

		JsonPath jp = new JsonPath(post("/1/auth/", lr).asString());
		String token = jp.getString("response.token");
		requestSpec.header("Authorization", "Token " + token);
		return this;

	}

}
