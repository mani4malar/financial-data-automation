package com.financial.automation.api;

import com.financial.automation.config.ConfigReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private final String baseUrl =
            ConfigReader.get("api.base.url");

    public Response get(String endpoint) {
        return given()
                .baseUri(baseUrl)
                .when()
                .get(endpoint);
    }

    public Response post(String endpoint, Object requestBody) {
        return given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(endpoint);
    }
}