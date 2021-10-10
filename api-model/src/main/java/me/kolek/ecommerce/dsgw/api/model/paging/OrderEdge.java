package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class OrderEdge extends Edge<OrderDTO> {

  @JsonCreator
  public OrderEdge(String cursor, OrderDTO node) {
    super(cursor, node);
  }
}
