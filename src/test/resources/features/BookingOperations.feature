Feature: Booking Operations

  Scenario: Create a booking
    Given I have a booking request with the following details:
      | date       | destination | origin | userId |
      | 2025-03-13 | CDG         | LHR    | 1      |
    When I send the booking creation request
    Then the booking response status code should be 201
    And the booking response should contain "Booking created successfully"

  Scenario: Create a booking with non-existing user
    Given I have a booking request with the following details:
      | date       | destination | origin | userId |
      | 2025-03-05 | LHR         | CDG    | 99998  |
    When I send the booking creation request
    Then the booking response status code should be 404
    And the booking response should contain "User not found"

  Scenario: Get bookings by user and date
    Given I have a request to get bookings for user ID 1 on date "2025-03-13"
    When I send the request to get bookings
    And I receive the booking response
    Then the booking response status code should be 200
    And the booking response should contain bookings for user 1

  Scenario: Get booking by ID
    Given I send a request to get the booking with ID 2
    When I receive the booking response
    Then the booking response status code should be 200
    And the booking response should contain "Booking retrieved successfully"

  Scenario: Get booking by non-existing ID
    Given I send a request to get the booking with ID 9999
    When I receive the booking response
    Then the booking response status code should be 404
    And the booking response should contain "No booking"
