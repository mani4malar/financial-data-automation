package com.financial.automation.tests.api_test;

import com.financial.automation.api.ApiClient;
import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.api.request.CreateUserRequest;
import io.restassured.response.Response;
import org.testng.Assert;
import java.time.Instant;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Covers successful and invalid POST API payloads.
 */
public class ApiPostTest {

    private final ApiClient client = new ApiClient();

    /**
     * Validates successful user creation.
     *
     * Checks:
     * - HTTP status
     * - response time
     * - response schema
     * - generated ID
     * - created timestamp
     * - request/response field consistency
     */
    @Test(groups = {"api", "regression"})
    public void shouldCreateUserSuccessfully() {

        CreateUserRequest request =
                new CreateUserRequest("morpheus", "leader");

        Response response =
                client.post("/api/users", request);

        Assert.assertEquals(
                response.statusCode(),
                201,
                "Expected HTTP status code 201"
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

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(
                        "schemas/create-user-response-schema.json"));

        Assert.assertNotNull(
                response.jsonPath().get("id"),
                "Created user ID should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("createdAt"),
                "Created timestamp should be present"
        );

        Assert.assertEquals(
                response.jsonPath().getString("name"),
                request.getName(),
                "Response name should match request"
        );

        Assert.assertEquals(
                response.jsonPath().getString("job"),
                request.getJob(),
                "Response job should match request"
        );

        Assert.assertTrue(
                response.jsonPath().get("id") instanceof String,
                "Created user ID should be a string"
        );

      String createdAt =
        response.jsonPath().getString("createdAt");

Assert.assertNotNull(
        createdAt,
        "Created timestamp should be present"
);

try {
    Instant.parse(createdAt);
} catch (Exception e) {
    Assert.fail(
            "Created timestamp is not a valid ISO-8601 timestamp: "
                    + createdAt
    );
}
    }

    /**
     * Validates API behavior for an invalid login payload.
     *
     * The password is intentionally omitted because it is
     * a mandatory field for the login operation.
     */
    @Test(groups = {"api", "regression"})
    public void shouldRejectInvalidLoginPayload() {

        String invalidPayload =
                """
                {
                    "email": "test@example.com"
                }
                """;

        Response response =
                client.post("/api/login", invalidPayload);

        Assert.assertEquals(
                response.statusCode(),
                400,
                "Expected HTTP status code 400 for invalid login payload"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("error"),
                "Error message should be present"
        );
    }
}