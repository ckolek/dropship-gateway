package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.PageInfo;

public class OrderConnection extends Connection<OrderDTO, OrderEdge> {

  @JsonCreator
  public OrderConnection(List<OrderEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
