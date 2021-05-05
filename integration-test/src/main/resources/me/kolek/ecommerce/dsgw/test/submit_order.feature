Feature: Submit Order
  An order can be submitted and processed

  Scenario:
    When A valid order is submitted
    Then A successful order action response is returned
    And The order exists with status NEW