Feature: User Operations

  Scenario: Create a new user
    Given I have a user creation request with the following details:
      | email                | name  | surname |
      | rash127875@gmail.com | peace | love    |
    When I send the user creation request
    Then the user response status code should be 201

  Scenario: Get all users
    Given I send a request to get all users
    When I receive the user response
    Then the user response status code should be 200
    And the user response should contain a list of users

  Scenario: Get user by ID
    Given I send a request to get the user with ID 3
    When I receive the user response
    Then the user response status code should be 200
    And the user details should contain email "rash00162@gmail.com"

  Scenario: Get user by ID not found
    Given I send a request to get the user with ID 9998
    When I receive the user response
    Then the user response status code should be 404
    And the user response should contain "User not found"
