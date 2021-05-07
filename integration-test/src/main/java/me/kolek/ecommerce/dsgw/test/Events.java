package me.kolek.ecommerce.dsgw.test;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;

@RequiredArgsConstructor
public class Events {
  private final Aws aws;
  private final Json json;

  private final Map<Type, List<OrderEventDTO>> eventsByType = new HashMap<>();

  @Given("The order events queue is purged")
  public void purgeOrderEventsQueue() {
    aws.purgeQueue("order-events");
    eventsByType.clear();
  }

  private void receiveOrderEvents() {
    aws.processMessages("order-events", Duration.ofSeconds(1), message -> {
      var orderEvent = json.parse(message.getBody(), OrderEventDTO.class);
      eventsByType.computeIfAbsent(orderEvent.getType(), k -> new ArrayList<>()).add(orderEvent);
    });
  }

  @Then("An order event of type {orderEventType} is emitted")
  public void checkOrderEventEmitted(OrderEventDTO.Type orderEventType) {
    checkOrderEventEmitted(orderEventType, null);
  }

  @Then("An order event of type {orderEventType} {orderEventSubType} is emitted")
  public void checkOrderEventEmitted(OrderEventDTO.Type orderEventType,
      OrderEventDTO.SubType orderEventSubType) {
    Resilience.retry(() -> {
      receiveOrderEvents();
      List<OrderEventDTO> events = eventsByType.getOrDefault(orderEventType, List.of());
      assertThat(events).isNotEmpty();
      assertThat(events).anySatisfy(event -> {
        if (orderEventSubType != null) {
          assertThat(event.getSubType()).isEqualTo(orderEventSubType);
        }
      });
    });
  }
}
