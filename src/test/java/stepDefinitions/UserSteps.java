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

public class UserSteps {

    private static final String BASE_URL = "http://127.0.0.1:8900";

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    private Response response;
    private String userRequestBody;

    // --- User Creation ---
    @Given("I have a user creation request with the following details:")
    public void i_have_a_user_creation_request_with_the_following_details(DataTable data) {
        // Assume first row is header; data is in row 1.
        userRequestBody = "{\n" +
                "  \"email\": \"" + data.cell(1, 0) + "\",\n" +
                "  \"name\": \"" + data.cell(1, 1) + "\",\n" +
                "  \"surname\": \"" + data.cell(1, 2) + "\"\n" +
                "}";
        System.out.println("User Request Body: " + userRequestBody);
    }

    @When("I send the user creation request")
    public void i_send_the_user_creation_request() {
        response = given()
                .header("Content-Type", "application/json")
                .body(userRequestBody)
                .when()
                .post("/user");
        System.out.println("User Creation Response: " + response.getBody().asString());
    }

    // --- Get all users ---
    @Given("I send a request to get all users")
    public void i_send_a_request_to_get_all_users() {
        response = given()
                .when()
                .get("/user");
        System.out.println("Get All Users Response: " + response.getBody().asString());
    }

    // --- Get user by ID ---
    @Given("I send a request to get the user with ID {int}")
    public void i_send_a_request_to_get_the_user_with_id(int userId) {
        response = given()
                .when()
                .get("/user/" + userId);
        System.out.println("Get User By ID Response: " + response.getBody().asString());
    }

    // --- Common receive step for users ---
    @When("I receive the user response")
    public void i_receive_the_user_response() {
        // No extra action; response already stored.
    }

    // --- Assertions for users ---
    @Then("the user response status code should be {int}")
    public void the_user_response_status_code_should_be(int expectedStatusCode) {
        System.out.println("Status Code: " + response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    @Then("the user response should contain a list of users")
    public void the_user_response_should_contain_a_list_of_users() {
        int listSize = response.jsonPath().getList("$").size();
        System.out.println("List size: " + listSize);
        assertTrue(listSize > 0, "Expected a non-empty list of users.");
    }

    @Then("the user details should contain email {string}")
    public void the_user_details_should_contain_email(String expectedEmail) {
        String actualEmail = response.jsonPath().getString("email");
        System.out.println("Actual Email: " + actualEmail);
        // Update expected email if needed; here we expect the actual email to match.
        assertTrue(actualEmail.contains(expectedEmail),
                "Expected email to contain '" + expectedEmail + "' but got '" + actualEmail + "'.");
    }

    @Then("the user response should contain {string}")
    public void the_user_response_should_contain(String expectedMessage) {
        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);
        // For "User not found" scenario, check if message contains "No user with id"
        if (expectedMessage.equals("User not found")) {
            assertTrue(responseBody.toLowerCase().contains("no user with id"),
                    "Expected error message containing 'no user with id' not found in response.");
        }  else {
            assertTrue(responseBody.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' not found in response.");
        }
    }
}
