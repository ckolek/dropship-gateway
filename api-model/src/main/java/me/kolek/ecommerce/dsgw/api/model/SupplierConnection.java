package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.PageInfo;

public class SupplierConnection extends Connection<SupplierDTO, SupplierEdge> {

  public SupplierConnection(List<SupplierEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
