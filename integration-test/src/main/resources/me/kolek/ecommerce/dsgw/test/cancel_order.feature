Feature: Cancel Order
  An order can be cancelled

  Background:
    Given The order events queue is purged

  Scenario:
    When A valid order is submitted
    Then A successful order action response is returned
    And The order exists with status NEW
    And An order event of type ORDER_CREATED is emitted
    When A request to cancel the order is submitted
    Then A successful order action response is returned
    And The order is cancelled
    And An order event of type ORDER_CANCELLED is emitted
    And The order can be found by ID with the correct status