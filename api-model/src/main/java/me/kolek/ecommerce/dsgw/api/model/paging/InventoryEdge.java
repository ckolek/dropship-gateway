package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.InventoryDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class InventoryEdge extends Edge<InventoryDTO> {

  @JsonCreator
  public InventoryEdge(InventoryDTO node) {
    super(null, node);
  }
}
