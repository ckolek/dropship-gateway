package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.PageInfo;

public class CarrierConnection extends Connection<CarrierDTO, CarrierEdge> {

  @JsonCreator
  public CarrierConnection(List<CarrierEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
