package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class SupplierEdge extends Edge<SupplierDTO> {

  public SupplierEdge(SupplierDTO node) {
    super(null, node);
  }
}
