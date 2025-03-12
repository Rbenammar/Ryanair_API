# QA API Challenge

## 📌 Project Overview
This project contains automated API tests for the **AppPerformance** application, which exposes a REST API with the following resources:
- **Users**: Create and manage users.
- **Bookings**: Associate bookings with users.

The API is tested using:
- **Java 17**
- **Maven**
- **TestNG**
- **RestAssured**
- **BDD (Cucumber)**

---

## 🚀 Setup & Installation
### **Prerequisites**
1. Install [Docker](https://docs.docker.com/get-docker/)
2. Install [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
3. Install [Maven](https://maven.apache.org/download.cgi)

### **Start the API**
1. Load the API image into Docker:
   ```bash
   docker load -i api_testing_service_latest.tar.xz
   
---
### 🚀 **Running the Tests**
The tests are implemented in Java using TestNG for test execution, RestAssured for handling API requests, and Cucumber for BDD-style tests.
These tests are located in the **src/test/java directory**.

### **TestRunner Class**
To execute all tests, run the TestRunner class. 
This class integrates Cucumber feature files with TestNG, ensuring that all tests are executed as part of the test suite.

### **Test Scenarios**
The following test scenarios are covered:

1. Creating a User
   API Tested: POST /user.

**Description**: Verifies that the API creates a user successfully when valid details are provided.

2. Creating a Booking
   API Tested: POST /booking.

**Description**: Validates that a booking is successfully associated with a user when the correct details are provided.

3. Fetching Bookings for a User
   API Tested: GET /booking.

**Description**: Ensures that all bookings for a specific user are retrieved successfully and validated against expected results.

4. Invalid User Handling
   API Tested: GET /user/{id}.

**Description**: Verifies that the API responds correctly with an error message when an invalid user ID is provided.