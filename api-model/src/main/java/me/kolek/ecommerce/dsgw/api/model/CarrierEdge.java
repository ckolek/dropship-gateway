package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class CarrierEdge extends Edge<CarrierDTO> {

  @JsonCreator
  public CarrierEdge(CarrierDTO node) {
    super(null, node);
  }
}
