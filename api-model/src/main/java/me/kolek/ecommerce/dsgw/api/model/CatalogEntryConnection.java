package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.PageInfo;

public class CatalogEntryConnection extends Connection<CatalogEntryDTO, CatalogEntryEdge> {

  public CatalogEntryConnection(List<CatalogEntryEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
