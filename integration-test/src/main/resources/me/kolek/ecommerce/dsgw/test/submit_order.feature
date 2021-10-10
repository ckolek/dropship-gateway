Feature: Submit Order
  An order can be submitted and processed

  Background:
    Given Client is authenticated as admin organization
    And The order events queue is purged

  Scenario: Successfully submit an order
    When A valid order is submitted
    Then A successful order action response is returned
    And The order is new
    And An order event of type ORDER_CREATED is emitted
    And The order can be found by ID with the correct status

  Scenario: Fail to submit an order with an existing order number
    When A valid order is submitted
    Then A successful order action response is returned
    And The order is new
    And An order event of type ORDER_CREATED is emitted
    And The order can be found by ID with the correct status
    When A duplicate order is submitted
    Then A failed order action response is returned with reasons:
      | description              |
      | order \S+ already exists |