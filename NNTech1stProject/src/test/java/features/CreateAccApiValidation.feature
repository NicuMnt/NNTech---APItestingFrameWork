Feature: Create NEW ACCOUNT


  Scenario:

    Given I have a valid set of prerequisites
    When I send Post request to create the account
    Then The response status should be 200
    Then I should store the account ID
