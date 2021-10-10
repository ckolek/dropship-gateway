package me.kolek.ecommerce.dsgw.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.paging.OrderConnection;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.OrderEdge;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;
import me.kolek.ecommerce.dsgw.test.api.GraphQLInvoker;
import me.kolek.ecommerce.dsgw.util.OrderUtil;

@RequiredArgsConstructor
public class OrderManagement {

  private final GraphQLInvoker graphQLInvoker;

  private OffsetDateTime requestTime;
  private OrderActionResult orderActionResult;
  private OrderDTO order;

  @When("A valid order is submitted")
  public void submitOrder() throws Exception {
    var request = SubmitOrder.validRequest();

    requestTime = OffsetDateTime.now();
    orderActionResult = graphQLInvoker
        .invoke("SubmitOrder", Map.of("request", request), OrderActionResult.class);
  }

  @When("A duplicate order is submitted")
  public void submitDuplicateOrder() throws Exception {
    var request = SubmitOrder.fromOrder(order);

    requestTime = OffsetDateTime.now();
    orderActionResult = graphQLInvoker
        .invoke("SubmitOrder", Map.of("request", request), OrderActionResult.class);
  }

  @When("A request to acknowledge the order is submitted")
  public void acknowledgeOrder() throws Exception {
    var request = AcknowledgeOrder.acceptAllLines(order);

    requestTime = OffsetDateTime.now();
    orderActionResult = graphQLInvoker.invoke("AcknowledgeOrder",
        Map.of("orderId", orderActionResult.getOrderId(), "request", request),
        OrderActionResult.class);
  }

  @When("A request to cancel the order is submitted")
  public void cancelOrder() throws Exception {
    var request = CancelOrderRequest.builder()
        .cancelCode("CXRO")
        .cancelReason("some reason")
        .build();

    requestTime = OffsetDateTime.now();
    orderActionResult = graphQLInvoker.invoke("CancelOrder",
        Map.of("orderId", orderActionResult.getOrderId(), "request", request),
        OrderActionResult.class);
  }

  @When("A request to ship part of the order is submitted")
  public void shipPartOfOrder() throws Exception {
    var request = ShipOrder.shipPartOfOrder(order);

    requestTime = OffsetDateTime.now();
    orderActionResult = graphQLInvoker.invoke("AddOrderShipment",
        Map.of("orderId", orderActionResult.getOrderId(), "request", request),
        OrderActionResult.class);
  }

  @When("A request to ship the remainder of the order is submitted")
  public void shipRemainderOfOrder() throws Exception {
    var request = ShipOrder.shipPartOfOrder(order);

    requestTime = OffsetDateTime.now();
    orderActionResult = graphQLInvoker.invoke("AddOrderShipment",
        Map.of("orderId", orderActionResult.getOrderId(), "request", request),
        OrderActionResult.class);
  }

  @When("A request to ship the entire order is submitted")
  public void shipEntireOrder() throws Exception {
    var request = ShipOrder.shipEntireOrder(order);

    requestTime = OffsetDateTime.now();
    orderActionResult = graphQLInvoker.invoke("AddOrderShipment",
        Map.of("orderId", orderActionResult.getOrderId(), "request", request),
        OrderActionResult.class);
  }

  private void assertThatOrderActionResultSatisfies(Consumer<OrderActionResult> requirements) {
    assertThat(orderActionResult).describedAs("result").isNotNull();
    assertThat(orderActionResult).describedAs("result").satisfies(requirements);
  }

  @Then("A successful order action response is returned")
  public void successfulOrderActionResponseWasReturned() {
    assertThatOrderActionResultSatisfies(result -> {
      String reasons = result.getReasons().stream().map(r -> " - " + r.getDescription())
          .collect(Collectors.joining("\n"));

      assertThat(result.getStatus()).withFailMessage("order action failed with reasons:\n%s",
          reasons).isSameAs(Status.SUCCESSFUL);

      assertThat(result.getOrderId()).describedAs("order ID").isNotBlank();
    });
  }

  @Then("A failed order action response is returned with reasons:")
  public void failedOrderActionResponseWasReturned(List<String> descriptions) {
    assertThatOrderActionResultSatisfies(result -> {
      assertThat(result.getStatus()).describedAs("status").withFailMessage("order action succeeded")
          .isSameAs(Status.FAILED);

      assertThat(result.getReasons()).describedAs("reasons").allSatisfy(reason -> {
        assertThat(descriptions).anyMatch(description -> reason.getDescription().matches(description));
      });
    });
  }

  private OrderActionResult getOrderActionResult() {
    if (orderActionResult == null) {
      throw new IllegalStateException("order action has not been invoked");
    }
    return orderActionResult;
  }

  @Then("The order exists with status {orderStatus}")
  public void checkOrderWithStatus(OrderDTO.Status orderStatus) throws Exception {
    order = Resilience
        .retry(() -> tryCheckOrderWithStatus(getOrderActionResult().getOrderId(), orderStatus));
  }

  @Then("The order is new")
  public void checkOrderIsNew() throws Exception {
    checkOrderWithStatus(OrderDTO.Status.NEW);

    assertThat(order.getTimeCreated()).describedAs("time created")
        .isBetween(requestTime, OffsetDateTime.now());
  }

  @Then("The order is acknowledged")
  public void checkOrderIsAcknowledged() throws Exception {
    checkOrderWithStatus(OrderDTO.Status.ACKNOWLEDGED);

    assertThat(order.getTimeAcknowledged()).describedAs("time acknowledged")
        .isBetween(requestTime, OffsetDateTime.now());
  }

  @Then("The order is cancelled")
  public void checkOrderIsCancelled() throws Exception {
    checkOrderWithStatus(OrderDTO.Status.CANCELLED);

    assertThat(order.getTimeCancelled()).describedAs("time cancelled")
        .isBetween(requestTime, OffsetDateTime.now());
    assertThat(order.getCancelCode()).describedAs("cancel code").isNotNull();
    assertThat(order.getCancelReason()).describedAs("cancel reason").isNotNull();
  }

  @Then("The order is partially shipped")
  public void checkOrderIsShippedPartial() throws Exception {
    checkOrderWithStatus(OrderDTO.Status.SHIPPED_PARTIAL);

    assertThat(order.getItems()).describedAs("items").allSatisfy(orderItem -> {
      switch (orderItem.getStatus()) {
        case NEW -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isZero();
        case ACKNOWLEDGED -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isZero();
        case CANCELLED -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isZero();
        case SHIPPED_PARTIAL -> assertThat(OrderUtil.getQuantityShipped(orderItem))
            .isStrictlyBetween(0, orderItem.getQuantityAccepted());
        case SHIPPED -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isEqualTo(
            orderItem.getQuantityAccepted());
        default -> fail("unexpected status " + orderItem.getStatus());
      }
    });
    assertThat(order.getPackages()).describedAs("packages").isNotEmpty();
  }

  @Then("The order is shipped")
  public void checkOrderIsShipped() throws Exception {
    checkOrderWithStatus(OrderDTO.Status.SHIPPED);

    assertThat(order.getItems()).describedAs("items").allSatisfy(orderItem -> {
      switch (orderItem.getStatus()) {
        case NEW -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isZero();
        case ACKNOWLEDGED -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isZero();
        case CANCELLED -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isZero();
        case SHIPPED -> assertThat(OrderUtil.getQuantityShipped(orderItem)).isEqualTo(
            orderItem.getQuantityAccepted());
        default -> fail("unexpected status " + orderItem.getStatus());
      }
    });
    assertThat(order.getPackages()).describedAs("packages").isNotEmpty();
  }

  private OrderDTO tryCheckOrderWithStatus(String id, OrderDTO.Status orderStatus) throws Exception {
    OrderDTO order = graphQLInvoker.invoke("GetOrder", Map.of("id", id), OrderDTO.class);
    assertThat(order).withFailMessage("order with ID %s not found", id).isNotNull();
    assertThat(order.getStatus()).describedAs("order status").isEqualTo(orderStatus);
    return order;
  }

  @Then("The order can be found by ID with the correct status")
  public void verifySearchOrderById() throws Exception {
    Resilience.retry(this::tryVerifySearchOrderById);
  }

  private void tryVerifySearchOrderById() throws Exception {
    OrderConnection connection = graphQLInvoker.invoke("SearchOrderById",
        Map.of("id", order.getId()), OrderConnection.class);
    assertThat(connection).describedAs("order connection").isNotNull();
    assertThat(connection.getPageInfo()).describedAs("pageInfo")
        .hasFieldOrPropertyWithValue("totalCount", 1L);
    assertThat(connection.getEdges()).describedAs("order edges").singleElement()
        .extracting(OrderEdge::getNode).describedAs("order node")
        .hasFieldOrPropertyWithValue("status", order.getStatus());
  }
}
