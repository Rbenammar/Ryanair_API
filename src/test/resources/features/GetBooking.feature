Feature: User Booking API Tests

  Scenario: User not found for bookings
    Given I search for bookings for a non-existing user with ID "79569" and date "2025-03-03"
    When I send a GET request to fetch the booking
    Then I should receive a 404 status code
    And I should see an error message saying "User not found"
