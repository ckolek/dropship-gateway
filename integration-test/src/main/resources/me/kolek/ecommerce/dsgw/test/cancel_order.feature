Feature: Cancel Order
  An order can be cancelled

  Scenario:
    When A valid order is submitted
    Then A successful order action response is returned
    And The order exists with status NEW
    When A request to cancel the order is submitted
    Then A successful order action response is returned
    And The order exists with status CANCELLED