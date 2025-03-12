package stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class BookingSteps {
    private static final String BASE_URL = "http://127.0.0.1:8900";
    private Response response;
    private String requestBody;

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    // --- Booking Creation ---
    @Given("I have a booking request with the following details:")
    public void bookingRequest(DataTable table) {
        // Use the data from the table (assumes header row then one row of data)
        requestBody = String.format("{\"date\":\"%s\",\"destination\":\"%s\",\"origin\":\"%s\",\"userId\":%s}",
                table.cell(1, 0), table.cell(1, 1), table.cell(1, 2), table.cell(1, 3));
    }

    @When("I send the booking creation request")
    public void sendBookingCreation() {
        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/booking");
    }

    // --- Generic status code assertion ---
    @Then("the response status code should be {int}")
    public void checkStatusCode(int expected) {
        assertEquals(response.getStatusCode(), expected);
    }

    // --- Successful response check: verify booking id exists ---
    @Then("the response should indicate success")
    public void responseSuccess() {
        assertNotNull(response.jsonPath().get("id"), "Expected booking id in response");
    }

    // --- Error message check ---
    @Then("the response should contain {string}")
    public void responseShouldContain(String expected) {
        String actual = response.jsonPath().getString("message");
        // Fallback to whole response if no "message" field is present
        if (actual == null) {
            actual = response.getBody().asString();
        }
        assertTrue(actual.contains(expected), "Expected response to contain: " + expected);
    }

    // --- Check that bookings for a specific user are present ---
    @Then("the response should contain bookings for user {int}")
    public void responseContainsBookingsForUser(int userId) {
        String body = response.getBody().asString();
        assertTrue(body.contains("\"userId\":" + userId), "Expected bookings for user " + userId);
    }

    // --- Creating a booking as a pre-requisite ---
    @Given("I have created a booking for user {int} on date {string} with details:")
    public void createBookingForUser(int userId, String date, DataTable table) {
        // Override the date and userId using the provided parameters.
        requestBody = String.format("{\"date\":\"%s\",\"destination\":\"%s\",\"origin\":\"%s\",\"userId\":%d}",
                date, table.cell(1, 1), table.cell(1, 2), userId);
        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/booking");
        assertEquals(response.getStatusCode(), 201);
    }

    // --- GET requests ---
    @When("I get bookings for user {int} on date {string}")
    public void getBookingsForUserOnDate(int userId, String date) {
        response = given()
                .queryParam("userId", userId)
                .queryParam("date", date)
                .when()
                .get("/booking");
    }

    @When("I get the booking with ID {int}")
    public void getBookingById(int bookingId) {
        response = given()
                .when()
                .get("/booking/" + bookingId);
    }
}
