package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class InventoryEdge extends Edge<InventoryDTO> {

  public InventoryEdge(InventoryDTO node) {
    super(null, node);
  }
}
