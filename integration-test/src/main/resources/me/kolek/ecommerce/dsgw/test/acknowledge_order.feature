Feature: Acknowledge Order
  An order can be acknowledged (accepted or rejected)

  Background:
    Given The order events queue is purged

  Scenario:
    When A valid order is submitted
    Then A successful order action response is returned
    And The order exists with status NEW
    And An order event of type ORDER_CREATED is emitted
    When A request to acknowledge the order is submitted
    Then A successful order action response is returned
    And The order is acknowledged
    And An order event of type ORDER_ACKNOWLEDGED is emitted
    And The order can be found by ID with the correct status
