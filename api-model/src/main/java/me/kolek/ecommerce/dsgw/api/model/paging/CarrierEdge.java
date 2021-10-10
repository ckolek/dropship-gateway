package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.CarrierDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class CarrierEdge extends Edge<CarrierDTO> {

  @JsonCreator
  public CarrierEdge(CarrierDTO node) {
    super(null, node);
  }
}
