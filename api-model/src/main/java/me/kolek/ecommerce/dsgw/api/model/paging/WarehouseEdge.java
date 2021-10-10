package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class WarehouseEdge extends Edge<WarehouseDTO> {

  @JsonCreator
  public WarehouseEdge(WarehouseDTO node) {
    super(null, node);
  }
}
