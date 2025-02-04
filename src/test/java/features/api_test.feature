Feature: "Test first api"

  Scenario: Verify the API response
    Given I send a GET request to "/api/hello"
    Then the response status should be 200
    And the response body should contain "Hello"