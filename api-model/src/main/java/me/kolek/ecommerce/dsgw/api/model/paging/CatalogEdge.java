package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class CatalogEdge extends Edge<CatalogDTO> {

  @JsonCreator
  public CatalogEdge(CatalogDTO node) {
    super(null, node);
  }
}
