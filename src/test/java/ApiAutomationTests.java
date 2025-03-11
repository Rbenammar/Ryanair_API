import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class ApiAutomationTests {


    private static final String BASE_URL = "http://127.0.0.1:8900/docs/"; // Local API URL

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }




    // Test for creating a new user
    @Test
    public void testCreateUser() {
        String requestBody = "{\n" +
                "  \"email\": \"user@example.com\",\n" +
                "  \"name\": \"John\",\n" +
                "  \"surname\": \"Doe\"\n" +
                "}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/user/addUser")  // Correct endpoint for creating a user
                .then()
                .statusCode(201)  // Expecting a 201 status code for successful creation
                .extract().response();

        // Assertions to verify the user creation
        response.then().body("id", notNullValue());
        response.then().body("email", equalTo("user@example.com"));
        response.then().body("name", equalTo("John"));
        response.then().body("surname", equalTo("Doe"));
    }

    // Test for fetching all users
    @Test
    public void testGetAllUsers() {
        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/user/getAllUsers")  // Correct endpoint for fetching all users
                .then()
                .statusCode(200)  // Expecting a 200 status code for success
                .extract().response();

        // Assertions to verify that the response contains a list of users
        response.then().body("$", isA(java.util.List.class));
        response.then().body("[0].id", notNullValue());
        response.then().body("[0].email", notNullValue());
        response.then().body("[0].name", notNullValue());
        response.then().body("[0].surname", notNullValue());
    }

    // Test for handling errors when creating a user with an existing email
    @Test
    public void testCreateUserWithExistingEmail() {
        String requestBody = "{\n" +
                "  \"email\": \"user@example.com\",\n" +
                "  \"name\": \"Jane\",\n" +
                "  \"surname\": \"Smith\"\n" +
                "}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/user/addUser")  // Correct endpoint for creating a user
                .then()
                .statusCode(409)  // Expecting a 409 status code for conflict (user already exists)
                .extract().response();

        // Assertions to verify the error response
        response.then().body("message", equalTo("User with given email exists already"));
    }
    }





//    private static String BASE_URL = "http://127.0.0.1:8900"; // Update with actual API URL
//    private static String userId;
//    private static String bookingId;
//
//    @BeforeClass
//    public void setup() {
//        RestAssured.baseURI = BASE_URL;
//    }
//
//    @Test(priority = 1)
//    public void testCreateUser() {
//        String requestBody = "{\"name\": \"ramy Dsir3\", \"email\": \"ramy2@example.com\"}";
//
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .body(requestBody)
//                .when()
//                .post("/user")
//                .then()
//                .statusCode(200)
//                .log().all()
//                .extract().response();
//
//        userId = response.jsonPath().getString("id");
//        Assert.assertNotNull(userId, "User ID should not be null");
//    }
//
//    @Test(priority = 2, dependsOnMethods = "testCreateUser")
//    public void testGetAllUsers() {
//        given()
//                .when()
//                .get("/user")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test(priority = 3, dependsOnMethods = "testCreateUser")
//    public void testGetUserById() {
//        given()
//                .when()
//                .get("/user/" + userId)
//                .then()
//                .statusCode(200)
//                .log().body();
//    }
//
//    @Test(priority = 4, dependsOnMethods = "testCreateUser")
//    public void testCreateBooking() {
//        String requestBody = "{\"userId\": \"" + userId + "\", \"date\": \"2025-03-10\"}";
//
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .body(requestBody)
//                .when()
//                .post("/booking")
//                .then()
//                .statusCode(201)
//                .extract().response();
//
//        bookingId = response.jsonPath().getString("id");
//        Assert.assertNotNull(bookingId, "Booking ID should not be null");
//    }
//
//    @Test(priority = 5, dependsOnMethods = "testCreateBooking")
//    public void testGetAllBookings() {
//        given()
//                .when()
//                .get("/booking")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test(priority = 6, dependsOnMethods = "testCreateBooking")
//    public void testGetBookingById() {
//        given()
//                .when()
//                .get("/booking/" + bookingId)
//                .then()
//                .statusCode(200)
//                .log().body();
//    }
//


//    @Test
//    public void testGetAllUsers() {
//        // 1. Test Get All Users (Success Scenario)
//        Response response = given()
//                .baseUri(BASE_URL)
//                .header("Accept", "application/json")
//                .when()
//                .get("/user")  // Correct endpoint for fetching all users
//                .then()
//                .statusCode(200)  // Expecting a 200 status code for success
//                .extract().response();
//
//        // Optionally, verify the response body (for example, checking if the response contains the expected user data)
//       // System.out.println("Get All Users Response: " + response.asString());
//
//        // 2. Test Get Users Error (Error Scenario - Invalid Endpoint)
//        Response errorResponse = given()
//                .baseUri(BASE_URL)
//                .header("Accept", "application/json")
//                .when()
//                .get("/invalid-endpoint");  // Simulating an invalid endpoint
//
//        // Log the error response details
//        System.out.println("Error Response Status Code: " + errorResponse.statusCode());
//        System.out.println("Error Response Body: " + errorResponse.asString());
//
//        // Check if the status code matches expectation for error
//        errorResponse.then().statusCode(404);  // Expected 404 for invalid endpoint
//    }
//
//
//    @Test
//    public void testGetUserById() {
//
//        int userId = 4;
//
//        // Make GET request to fetch user by ID
//        Response response = given()
//                .baseUri(BASE_URL)  // Base URL
//                .header("accept", "application/json")  // Accept header for JSON response
//                .when()
//                .get("/user/" + userId);  // GET request with user ID parameter
//
//        // Print the response for debugging
//        System.out.println("Response Body: " + response.getBody().asString());
//
//        // Handle different response codes
//        switch (response.getStatusCode()) {
//            case 200:  // User found
//                System.out.println("User found. Response Code: 200");
//                System.out.println("User Info: " + response.jsonPath().getString("email"));
//                break;
//            case 404:  // User not found
//                System.out.println("User not found. Response Code: 404");
//                break;
//            default:  // Default error handling
//                System.out.println("Error: " + response.jsonPath().getString("message"));
//                break;
//        }
//    }