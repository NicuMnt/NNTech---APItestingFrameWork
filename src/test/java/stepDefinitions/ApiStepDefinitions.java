package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import com.google.gson.Gson;
import io.restassured.http.ContentType;


public class ApiStepDefinitions {

    private Response response;
    private static final String BASE_URL = "http://localhost:8080";

    @Given("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = RestAssured.get(BASE_URL + endpoint); // Update with your API URL
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatus) {
        assertEquals(expectedStatus.intValue(), response.getStatusCode());
    }

    @Then("the response body should contain {string}")
    public void the_response_body_should_contain(String expectedText) {
        String responseBody = response.getBody().asString();

        // Debugging: Print the response
        System.out.println("Actual Response Body: " + responseBody);
        // Assertion
        assertTrue("Response does not contain expected text!", responseBody.contains(expectedText));
    }

    @When("I send a POST request to {string} with body")
    public void i_send_a_post_request_to_with_body(String endpoint, Map<String, String> requestBody) {
        String jsonBody = new Gson().toJson(requestBody); // Convert Map to JSON

        response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(endpoint);
    }

    @When("I send a POST request to {string} with payload")
    public void sendPostRequest(String endpoint, String payload) {
        // Code for POST request
    }




}