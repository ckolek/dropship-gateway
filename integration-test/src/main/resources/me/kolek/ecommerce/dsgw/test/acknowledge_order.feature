Feature: Submit Order
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
    And The order exists with status ACKNOWLEDGED
    And An order event of type ORDER_ACKNOWLEDGED is emitted
