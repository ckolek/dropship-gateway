package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.CarrierDTO;

public class CarrierConnection extends Connection<CarrierDTO, CarrierEdge> {

  @JsonCreator
  public CarrierConnection(List<CarrierEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
