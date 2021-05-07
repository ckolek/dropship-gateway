Feature: Submit Order
  An order can be submitted and processed

  Background:
    Given The order events queue is purged

  Scenario:
    When A valid order is submitted
    Then A successful order action response is returned
    And The order exists with status NEW
    And An order event of type ORDER_CREATED is emitted