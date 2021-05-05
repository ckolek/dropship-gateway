package me.kolek.ecommerce.dsgw.test;

import io.cucumber.java.ParameterType;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;

public class Transformers {

  @ParameterType(value = "[A-Z_]+")
  public OrderDTO.Status orderStatus(String string) {
    return OrderDTO.Status.valueOf(string);
  }
}
