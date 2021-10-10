Feature: Cancel Order
  An order can be cancelled

  Background:
    Given Client is authenticated as admin organization
    And The order events queue is purged

  Scenario: Successfully cancel order
    When A valid order is submitted
    Then A successful order action response is returned
    And The order is new
    And An order event of type ORDER_CREATED is emitted
    When A request to cancel the order is submitted
    Then A successful order action response is returned
    And The order is cancelled
    And An order event of type ORDER_CANCELLED is emitted
    And The order can be found by ID with the correct status

  Scenario: Fail to cancel an already cancelled order
    When A valid order is submitted
    Then A successful order action response is returned
    And The order is new
    And An order event of type ORDER_CREATED is emitted
    When A request to cancel the order is submitted
    Then A successful order action response is returned
    And The order is cancelled
    And An order event of type ORDER_CANCELLED is emitted
    When A request to cancel the order is submitted
    Then A failed order action response is returned with reasons:
      | description                      |
      | order has already been cancelled |
    And The order exists with status CANCELLED
    And The order can be found by ID with the correct status

  Scenario: Fail to cancel an acknowledged order
    When A valid order is submitted
    Then A successful order action response is returned
    And The order is new
    And An order event of type ORDER_CREATED is emitted
    When A request to acknowledge the order is submitted
    Then A successful order action response is returned
    And The order is acknowledged
    And An order event of type ORDER_ACKNOWLEDGED is emitted
    When A request to cancel the order is submitted
    Then A failed order action response is returned with reasons:
      | description                                        |
      | order with status ACKNOWLEDGED cannot be cancelled |
    And The order exists with status ACKNOWLEDGED
    And The order can be found by ID with the correct status

  Scenario: Fail to cancel a shipped order
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
    When A request to cancel the order is submitted
    Then A failed order action response is returned with reasons:
      | description                                           |
      | order with status SHIPPED_PARTIAL cannot be cancelled |
    And The order exists with status SHIPPED_PARTIAL
    And The order can be found by ID with the correct status