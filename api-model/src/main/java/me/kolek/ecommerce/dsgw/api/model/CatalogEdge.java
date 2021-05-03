package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class CatalogEdge extends Edge<CatalogDTO> {

  public CatalogEdge(CatalogDTO node) {
    super(null, node);
  }
}
