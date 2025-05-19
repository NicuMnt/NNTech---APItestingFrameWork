Feature: Create NEW ACCOUNT


  Scenario:

    Given I have a valid set of prerequisites
    When I send Post request to create the account
    Then The response status should be 200
    Then I should store the account ID
    Given I login in with unique data
    When I send Post request to login
    Then The login response status should be 200
    Then I store the access token and CSRF token from the login response

