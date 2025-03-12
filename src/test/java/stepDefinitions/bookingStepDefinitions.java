package stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class bookingStepDefinitions {
    private static final String BASE_URL = "http://127.0.0.1:8900";  // Update if needed

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    private Response response;
    private String bookingRequestBody;
    private int queryUserId;
    private String queryDate;

    // --- Booking Creation ---
    @Given("I have a booking request with the following details:")
    public void i_have_a_booking_request_with_the_following_details(DataTable data) {
        // Assume first row is header; data is in row 1.
        bookingRequestBody = "{\n" +
                "  \"date\": \"" + data.cell(1, 0) + "\",\n" +
                "  \"destination\": \"" + data.cell(1, 1) + "\",\n" +
                "  \"origin\": \"" + data.cell(1, 2) + "\",\n" +
                "  \"userId\": " + data.cell(1, 3) + "\n" +
                "}";
        System.out.println("Booking Request Body: " + bookingRequestBody);
    }

    @When("I send the booking creation request")
    public void i_send_the_booking_creation_request() {
        response = given()
                .header("Content-Type", "application/json")
                .body(bookingRequestBody)
                .when()
                .post("/booking");
        System.out.println("Booking Creation Response: " + response.getBody().asString());
    }

    // --- Get bookings by user and date ---
    @Given("I have a request to get bookings for user ID {int} on date {string}")
    public void i_have_a_request_to_get_bookings_for_user_id_on_date(int userId, String date) {
        this.queryUserId = userId;
        this.queryDate = date;
        System.out.println("Setting queryUserId: " + queryUserId);  // Debugging print
    }

    @When("I send the request to get bookings")
    public void i_send_the_request_to_get_bookings() {
        response = given()
                .queryParam("userId", queryUserId)  // Use "userId" as parameter
                .queryParam("date", queryDate)
                .when()
                .get("/booking");
        System.out.println("Get Bookings Response: " + response.getBody().asString());
    }

    // --- Get booking by ID ---
    @Given("I send a request to get the booking with ID {int}")
    public void i_send_a_request_to_get_the_booking_with_id(int bookingId) {
        response = given()
                .when()
                .get("/booking/" + bookingId);
        System.out.println("Get Booking By ID Response: " + response.getBody().asString());
    }

    // --- Common receive step for bookings ---
    @When("I receive the booking response")
    public void i_receive_the_booking_response() {
        // No extra action; response already stored.
    }

    // --- Assertions for bookings ---
    @Then("the booking response status code should be {int}")
    public void the_booking_response_status_code_should_be(int expectedStatusCode) {
        System.out.println("Status Code: " + response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    @Then("the booking response should contain {string}")
    public void the_booking_response_should_contain(String expectedMessage) {
        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);
        // For creation and retrieval, if the expected message is a success message,
        // we verify that the response contains an "id" field.
        if(expectedMessage.equals("Booking created successfully") || expectedMessage.equals("Booking retrieved successfully")){
            Object id = response.jsonPath().get("id");
            assertNotNull(id, "Expected booking id to be present in response");
        }
        // For error scenarios.
        else if (expectedMessage.equals("User not found")) {
            String actualMessage = response.jsonPath().getString("message");
            // Debugging print
            System.out.println("Actual Error Message: " + actualMessage);
            System.out.println("Expected User ID: " + queryUserId);

            Assert.assertTrue(actualMessage.contains("No user with id"),
                    "Expected error message to contain 'No user with id', but found: " + actualMessage);
        }
        else {
            assertTrue(responseBody.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' not found in response: " + responseBody);
        }
    }

    @Then("the booking response should contain bookings for user {int}")
    public void the_booking_response_should_contain_bookings_for_user(int userId) {
        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);
        assertTrue(responseBody.contains("\"userId\":" + userId),
                "No booking found for user " + userId + " in response: " + responseBody);
    }
}
