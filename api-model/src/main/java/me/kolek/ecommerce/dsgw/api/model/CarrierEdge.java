package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class CarrierEdge extends Edge<CarrierDTO> {

  public CarrierEdge(CarrierDTO node) {
    super(null, node);
  }
}
