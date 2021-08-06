package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class CatalogEntryEdge extends Edge<CatalogEntryDTO> {

  @JsonCreator
  public CatalogEntryEdge(CatalogEntryDTO node) {
    super(null, node);
  }
}
