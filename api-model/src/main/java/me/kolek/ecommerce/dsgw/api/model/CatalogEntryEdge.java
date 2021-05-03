package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class CatalogEntryEdge extends Edge<CatalogEntryDTO> {

  public CatalogEntryEdge(CatalogEntryDTO node) {
    super(null, node);
  }
}
