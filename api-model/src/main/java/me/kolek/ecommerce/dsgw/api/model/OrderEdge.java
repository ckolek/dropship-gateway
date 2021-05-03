package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class OrderEdge extends Edge<OrderDTO> {

  public OrderEdge(OrderDTO node) {
    super(null, node);
  }
}
