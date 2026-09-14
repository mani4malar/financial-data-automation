package com.financial.automation.tests.api_test;

import com.financial.automation.api.ApiClient;
import com.financial.automation.config.ConfigReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ApiGetTest {

    private final ApiClient client = new ApiClient();

    @Test(groups = {"api", "regression"})
    public void shouldGetUserSuccessfully() {

        Response response = client.get("/api/users/2");

        response.then()
        .assertThat()
        .body(matchesJsonSchemaInClasspath(
                "schemas/user-response-schema.json"));

        Assert.assertEquals(
                response.statusCode(),
                200,
                "Expected HTTP status code 200"
        );

        long responseTimeThreshold =
                Long.parseLong(
                        ConfigReader.get(
                                "api.response.time.threshold.ms"));

        Assert.assertTrue(
                response.time() < responseTimeThreshold,
                "Response time exceeded configured threshold of "
                        + responseTimeThreshold + " ms"
        );

        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Response data object should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("support"),
                "Response support object should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("data.id"),
                "User ID should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("data.email"),
                "User email should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("data.first_name"),
                "First name should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("data.last_name"),
                "Last name should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("data.avatar"),
                "Avatar should be present"
        );

        Assert.assertTrue(
                response.jsonPath().get("data.id") instanceof Integer,
                "User ID should be an integer"
        );

        Assert.assertTrue(
                response.jsonPath().get("data.email") instanceof String,
                "Email should be a string"
        );

        Assert.assertTrue(
                response.jsonPath().get("data.first_name") instanceof String,
                "First name should be a string"
        );

        Assert.assertTrue(
                response.jsonPath().get("data.last_name") instanceof String,
                "Last name should be a string"
        );

        Assert.assertTrue(
                response.jsonPath().get("data.avatar") instanceof String,
                "Avatar should be a string"
        );

        Assert.assertNotNull(
                response.jsonPath().get("support.url"),
                "Support URL should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("support.text"),
                "Support text should be present"
        );

        Assert.assertTrue(
                response.jsonPath().get("support.url") instanceof String,
                "Support URL should be a string"
        );

        Assert.assertTrue(
                response.jsonPath().get("support.text") instanceof String,
                "Support text should be a string"
        );
    }

    @Test(groups = {"api", "regression"})
public void shouldReturnNotFoundForInvalidUser() {

    Response response = client.get("/api/users/23");

    Assert.assertEquals(
            response.statusCode(),
            404,
            "Expected HTTP status code 404 for non-existent user"
    );

    long responseTimeThreshold =
            Long.parseLong(
                    ConfigReader.get(
                            "api.response.time.threshold.ms"));

    Assert.assertTrue(
            response.time() < responseTimeThreshold,
            "Response time exceeded configured threshold of "
                    + responseTimeThreshold + " ms"
    );
}
}