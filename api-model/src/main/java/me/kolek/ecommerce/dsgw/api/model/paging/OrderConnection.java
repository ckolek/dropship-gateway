package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;

public class OrderConnection extends Connection<OrderDTO, OrderEdge> {

  @JsonCreator
  public OrderConnection(List<OrderEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
