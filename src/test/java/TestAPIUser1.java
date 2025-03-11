import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class TestAPIUser1 {

    private static final String BASE_URL = "http://127.0.0.1:8900"; // Local API URL
    private static String userId;
    private static String bookingId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void testCreateUser() {
        // Define the request payload for creating a new user
        String requestBody = "{\n" +
                "  \"email\": \"ushohr@example.com\",\n" +
                "  \"name\": \"pokp\",\n" +
                "  \"surname\": \"pokpj\"\n" +
                "}";

        // Send POST request to create user
        Response response = given()
                .header("accept", "application/json")  // Set accept header
                .header("Content-Type", "application/json")  // Set content type header
                .body(requestBody)  // Set request body
                .when()
                .post("/user");  // Send POST request to /user endpoint

        // Print the response body for debugging
        //System.out.println("Response Body: " + response.getBody().asString());

        // Check the response status code and handle accordingly
        int statusCode = response.getStatusCode();
        if (statusCode == 201) {
            System.out.println("User created successfully. Response Code: 201");
            // Optionally, validate the response body here for 201 (e.g., check email and id)
        } else if (statusCode == 409) {
            // Extract error details for the 409 conflict response
            String message = response.jsonPath().getString("message");
            System.out.println("Conflict: " + message + ". Response Code: 409");

        } else {
            // Default error case (could be 400, 500, etc.)
            String errorDetails = response.jsonPath().getString("errors");
            System.out.println("Error Details: " + errorDetails);
        }
    }


    @Test
    public void testGetAllUsers_Success() {
        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/user")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Get All Users: " + response.asString());
        assertFalse(response.jsonPath().getList("$").isEmpty()); // Ensure users exist

    }

    @Test
    public void testGetAllUsers_InvalidEndpoint() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/invalid-endpoint")
                .then()
                .statusCode(404);

        System.out.println(" Invalid Endpoint: 404 Not Found");
    }

    @Test
    public void testGetUserById() {
        int userId = 4;

        // ✅ Test fetching user by ID
        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/user/" + userId)
                .then()
                .extract().response();

        if (response.getStatusCode() == 200) {
            System.out.println("✅ User found: " + response.jsonPath().getString("email"));
            assertNotNull(response.jsonPath().getString("email"), "Email should not be null");
        } else {
            System.out.println("User not found or other error.");
            assertEquals(response.getStatusCode(), 404, "Expected 404 if user does not exist");
        }
    }


    @Test
    public void testCreateBooking_Success() {
        // Request body for a valid booking
        String requestBody = """
                {
                        "date": "2025-03-03",
                        "destination": "CDG",
                       "origin": "LHR",
                           "userId": 1
                }
                """;

        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/booking");

        // Assert successful creation
        response.then().statusCode(201);
        System.out.println("Booking created successfully!");
    }

    @Test
    public void testCreateBooking_UserNotFound() {
        // Request body with a non-existing user ID
        String requestBody = """
                {
                     "date": "2025-03-03",
                     "destination": "LHR",
                     "origin": "CDG",
                        "userId": 9999
                }
                """;

        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/booking");

        // Assert user not found error
        response.then().statusCode(404);
        System.out.println(" Error handled: User not found.");
    }


    @Test
    public void testGetBookings_Success() {
        // Define query parameters
        int userId = 1;
        String date = "2025-03-03";

        Response response = given()
                .header("Accept", "application/json")
                .queryParam("user", userId)
                .queryParam("date", date)
                .when()
                .get("/booking");

        // Assert successful response
        response.then().statusCode(200);
        System.out.println("Bookings retrieved successfully!");
    }


    @Test
    public void testGetBookings_UserNotFound() {
        // Define query parameters with a non-existing userId
        int userId = 99987;  // Assuming this user does not exist
        String date = "2025-03-03";

        Response response = given()
                .header("Accept", "application/json")
                .queryParam("user", userId)  // Add userId to the request
                .queryParam("date", date)    // Add date to the request
                .when()
                .get("/booking");

        // Assert user not found error
        response.then().statusCode(404);
        System.out.println("Error handled: User not found.");
    }


    @Test
    public void testGetBookingById_Success() {
        // Define booking ID (valid ID)
        int bookingId = 3;

        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + bookingId);  // Use the booking ID in the URL

        // Assert successful response
        response.then().statusCode(200);
        System.out.println("Booking retrieved successfully!");
    }


    @Test
    public void testGetBookingById_NotFound() {
        int bookingId = 9999; // Non-existing booking

        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + bookingId);

        // Assert 404 Not Found
        response.then().statusCode(404);

        System.out.println("Error handled: Booking not found with correct response structure.");
    }

}
