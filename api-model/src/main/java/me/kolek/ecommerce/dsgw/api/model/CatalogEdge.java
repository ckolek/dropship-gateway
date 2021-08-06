package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class CatalogEdge extends Edge<CatalogDTO> {

  @JsonCreator
  public CatalogEdge(CatalogDTO node) {
    super(null, node);
  }
}
