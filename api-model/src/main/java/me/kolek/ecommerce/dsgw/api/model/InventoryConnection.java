package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.PageInfo;

public class InventoryConnection extends Connection<InventoryDTO, InventoryEdge> {

  public InventoryConnection(List<InventoryEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
