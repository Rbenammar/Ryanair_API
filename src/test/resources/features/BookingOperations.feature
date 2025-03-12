Feature: Booking Operations

  Scenario: Create a booking
    Given I have a booking request with the following details:
      | date       | destination | origin | userId |
      | 2025-03-12 | TYR         | BHG    | 1      |
    When I send the booking creation request
    Then the response status code should be 201
    And the response should indicate success

  Scenario: Create a booking with non-existing user
    Given I have a booking request with the following details:
      | date       | destination | origin | userId |
      | 2025-03-12 | TYR         | BHG    | 55     |
    When I send the booking creation request
    Then the response status code should be 404
    And the response should contain "No user with id"

  Scenario: Create a booking with invalid origin/destination format
    Given I have a booking request with the following details:
      | date       | destination | origin | userId |
      | 2025-03-12 | TYR         | BHGY   | 5      |
    When I send the booking creation request
    Then the response status code should be 400
    And the response should contain "Validation errors"

  Scenario: Get bookings by user and date
    Given I have created a booking for user 1 on date "2025-03-13" with details:
      | date       | destination | origin | userId |
      | 2025-03-12 | ABC         | XYZ    | 1      |
    When I get bookings for user 1 on date "2025-03-13"
    Then the response status code should be 200
    And the response should contain bookings for user 1

  Scenario: Get booking by ID
    When I get the booking with ID 2
    Then the response status code should be 200
    And the response should indicate success

  Scenario: Get booking by non-existing ID
    When I get the booking with ID 9999
    Then the response status code should be 404
    And the response should contain "No booking"
