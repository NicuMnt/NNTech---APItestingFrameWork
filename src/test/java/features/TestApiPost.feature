Feature: Test if Post works

  Scenario: Verify the API POST response
    When I send a POST request to "/api/hello" with body
      | key   | value  |
      | name  | Nicu   |
      | email | nicumunteanu21@gmail.com |
    Then the response status should be 200