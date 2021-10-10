package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class SupplierEdge extends Edge<SupplierDTO> {

  @JsonCreator
  public SupplierEdge(SupplierDTO node) {
    super(null, node);
  }
}
