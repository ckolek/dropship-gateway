package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;

public class CatalogEntryConnection extends Connection<CatalogEntryDTO, CatalogEntryEdge> {

  @JsonCreator
  public CatalogEntryConnection(List<CatalogEntryEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
