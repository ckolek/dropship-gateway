package me.kolek.ecommerce.dsgw.test;

import io.cucumber.java.ParameterType;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO;

public class Transformers {

  @ParameterType(value = "[A-Z_]+")
  public OrderDTO.Status orderStatus(String string) {
    return OrderDTO.Status.valueOf(string);
  }

  @ParameterType(value = "[A-Z_]+")
  public OrderEventDTO.Type orderEventType(String string) {
    return OrderEventDTO.Type.valueOf(string);
  }

  @ParameterType(value = "[A-Z_]+")
  public OrderEventDTO.SubType orderEventSubType(String string) {
    return OrderEventDTO.SubType.valueOf(string);
  }
}
