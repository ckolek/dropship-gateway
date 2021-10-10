Feature: Ship Order
  An order can be shipped

  Background:
    Given Client is authenticated as admin organization
    And The order events queue is purged

  Scenario: An order can be partially shipped
    When A valid order is submitted
    Then A successful order action response is returned
    And The order is new
    And An order event of type ORDER_CREATED is emitted
    When A request to acknowledge the order is submitted
    Then A successful order action response is returned
    And The order is acknowledged
    And An order event of type ORDER_ACKNOWLEDGED is emitted
    When A request to ship part of the order is submitted
    Then A successful order action response is returned
    And The order is partially shipped
    And An order event of type ORDER_SHIPPED_PARTIAL is emitted
    And The order can be found by ID with the correct status

  Scenario: An order can be completely shipped
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