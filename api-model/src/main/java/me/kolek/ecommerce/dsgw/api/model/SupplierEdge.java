package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class SupplierEdge extends Edge<SupplierDTO> {

  @JsonCreator
  public SupplierEdge(SupplierDTO node) {
    super(null, node);
  }
}
