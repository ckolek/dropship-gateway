package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class InventoryEdge extends Edge<InventoryDTO> {

  @JsonCreator
  public InventoryEdge(InventoryDTO node) {
    super(null, node);
  }
}
