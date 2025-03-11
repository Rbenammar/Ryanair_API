package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BookingSteps {
    private Response response;

    // Ensure to set the base URI
    static {
        RestAssured.baseURI = "http://127.0.0.1:8900";  // Replace with your actual API URL
    }

    @Given("I search for bookings for a non-existing user with ID {string} and date {string}")
    public void search_for_non_existing_user_bookings(String userId, String date) {
        response = given()
                .header("Accept", "application/json")
                .queryParam("user", userId)
                .queryParam("date", date)
                .when()
                .get("/booking");
    }

    @When("I send a GET request to fetch the booking")
    public void send_get_request_to_fetch_booking() {
        // Already sent in the @Given step
    }

    @Then("I should receive a {int} status code")
    public void should_receive_status_code(int statusCode) {
        System.out.println("Actual Status Code: " + response.getStatusCode());  // Debugging
        System.out.println("Response Body: " + response.getBody().asString());  // Debugging
        assertEquals(response.getStatusCode(), statusCode);
    }

    @Then("I should see an error message saying {string}")
    public void should_see_error_message(String errorMessage) {
        // Check for error message in case of 404 response
        if (response.getStatusCode() == 404) {
            String actualMessage = response.jsonPath().getString("message");
            assertTrue(actualMessage.toLowerCase().contains("no user with id"),
                    "Expected error message to contain: 'No user with id' but got: '" + actualMessage + "'");
        } else {
            // If status is 200 and there is data, check for bookings
            assertTrue(response.body().asString().contains("id"));
        }
    }
}
