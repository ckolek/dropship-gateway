package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class CatalogEntryEdge extends Edge<CatalogEntryDTO> {

  @JsonCreator
  public CatalogEntryEdge(CatalogEntryDTO node) {
    super(null, node);
  }
}
