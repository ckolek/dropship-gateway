package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class WarehouseEdge extends Edge<WarehouseDTO> {

  @JsonCreator
  public WarehouseEdge(WarehouseDTO node) {
    super(null, node);
  }
}
