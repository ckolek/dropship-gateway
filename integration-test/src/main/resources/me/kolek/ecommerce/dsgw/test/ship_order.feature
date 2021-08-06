Feature: Ship Order
  An order can be shipped

  Background:
    Given The order events queue is purged

  Scenario:
    When A valid order is submitted
    Then A successful order action response is returned
    And The order is new
    And An order event of type ORDER_CREATED is emitted
    When A request to acknowledge the order is submitted
    Then A successful order action response is returned
    And The order is acknowledged
    And An order event of type ORDER_ACKNOWLEDGED is emitted
    When A request to ship the entire order is submitted
    Then A successful order action response is returned
    And The order is shipped
    And An order event of type ORDER_SHIPPED is emitted
    And The order can be found by ID with the correct status