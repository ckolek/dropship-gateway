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
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.api.model.event.OrderEventDTO;
import me.kolek.ecommerce.dsgw.api.model.event.OrderEventDTO.Type;

@RequiredArgsConstructor
@Slf4j
public class Events {
  private final Aws aws;
  private final Json json;

  private final Map<Type, List<OrderEventDTO>> orderEventsByType = new HashMap<>();

  @Given("The order events queue is purged")
  public void purgeOrderEventsQueue() {
    aws.purgeQueue("order-events");
    orderEventsByType.clear();
  }

  private void receiveOrderEvents() {
    aws.processMessages("order-events", Duration.ofSeconds(1), message -> {
      var orderEvent = json.parse(message.getBody(), OrderEventDTO.class);

      log.debug("received order event message with type {}", orderEvent.getType());

      orderEventsByType.computeIfAbsent(orderEvent.getType(), k -> new ArrayList<>()).add(orderEvent);
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
      List<OrderEventDTO> events = orderEventsByType.getOrDefault(orderEventType, List.of());
      assertThat(events).isNotEmpty();
      assertThat(events).anySatisfy(event -> {
        if (orderEventSubType != null) {
          assertThat(event.getSubType()).isEqualTo(orderEventSubType);
        }
      });
    });
  }
}
