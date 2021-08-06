package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.PageInfo;

public class CatalogConnection extends Connection<CatalogDTO, CatalogEdge> {

  @JsonCreator
  public CatalogConnection(List<CatalogEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
