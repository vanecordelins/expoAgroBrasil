Feature: Login
  Perform login on email and password are inputted

  Scenario Outline: Input email and password in correct format
    Given I have a LoginActivity
    When I input click <email>
    And I select <password>
    And I press submit <button>
    Then I should <see> item

    Examples:
      | email              | password   | see   |
      | espresso@spoon.com | bananacake | true  |
      | espresso@spoon.com | lemoncake  | false |
      | latte@spoon.com    | lemoncake  | true  |