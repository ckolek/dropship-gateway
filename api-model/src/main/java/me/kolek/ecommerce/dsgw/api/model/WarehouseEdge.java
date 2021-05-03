package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class WarehouseEdge extends Edge<WarehouseDTO> {

  public WarehouseEdge(WarehouseDTO node) {
    super(null, node);
  }
}
