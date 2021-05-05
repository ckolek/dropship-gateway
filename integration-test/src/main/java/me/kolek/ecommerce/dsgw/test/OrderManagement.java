package me.kolek.ecommerce.dsgw.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.resilience4j.retry.Retry;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRecipient;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.test.api.GraphQLInvoker;

@RequiredArgsConstructor
public class OrderManagement {

  private final GraphQLInvoker graphQLInvoker;

  private OrderActionResult orderActionResult;

  @When("A valid order is submitted")
  public void submitOrder() throws Exception {
    String customerOrderNumber = Long.toString(System.currentTimeMillis() % 1000000);

    var request = SubmitOrderRequest.builder()
        .orderNumber(customerOrderNumber + "-1")
        .customerOrderNumber(customerOrderNumber)
        .warehouseCode("CWK1")
        .recipient(SubmitOrderRecipient.builder()
            .name("Chris Kolek")
            .email("ckolek@gmail.com")
            .phone("978-846-4525")
            .line1("100 Heard Street")
            .line2("Unit 314")
            .city("Chelsea")
            .state("MA")
            .postalCode("02150")
            .country("USA")
            .build())
        .item(SubmitOrderItem.builder()
            .sku("CK-1001")
            .quantity(10)
            .build())
        .carrierName("FedEx")
        .carrierMode("Home Delivery")
        .timeOrdered(OffsetDateTime.now().minusHours(6))
        .timeReleased(OffsetDateTime.now())
        .build();

    orderActionResult = graphQLInvoker
        .invoke("SubmitOrder", Map.of("request", request), OrderActionResult.class);
  }

  @When("A request to cancel the order is submitted")
  public void cancelOrder() throws Exception {
    var request = CancelOrderRequest.builder()
        .cancelCode("CXRO")
        .cancelReason("some reason")
        .build();

    orderActionResult = graphQLInvoker.invoke("CancelOrder",
        Map.of("orderId", orderActionResult.getOrderId(), "request", request),
        OrderActionResult.class);
  }

  @Then("A successful order action response is returned")
  public void orderWithIdWasReturned() {
    assertThat(orderActionResult).isNotNull();
    if (orderActionResult.getStatus() != Status.SUCCESSFUL) {
      fail("order action failed with reasons:\n%s",
          orderActionResult.getReasons().stream().map(r -> " - " + r.getDescription())
              .collect(Collectors.joining("\n")));
    }
    assertThat(orderActionResult.getOrderId()).isNotBlank();
  }

  @Then("The order exists with status {orderStatus}")
  public void checkOrderWithStatus(OrderDTO.Status orderStatus) throws Exception {
    Resilience.retry(() -> tryCheckOrderWithStatus(orderActionResult.getOrderId(), orderStatus));
  }

  private void tryCheckOrderWithStatus(String id, OrderDTO.Status orderStatus) throws Exception {
    OrderDTO order = graphQLInvoker.invoke("GetOrder", Map.of("id", id), OrderDTO.class);
    assertThat(order).withFailMessage("order with ID %s not found", id).isNotNull();
    assertThat(order.getStatus()).isEqualTo(orderStatus);
  }
}
