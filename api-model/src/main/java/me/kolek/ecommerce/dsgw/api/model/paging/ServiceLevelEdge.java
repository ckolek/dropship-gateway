package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;

public class ServiceLevelEdge extends Edge<ServiceLevelDTO> {

  @JsonCreator
  public ServiceLevelEdge(ServiceLevelDTO node) {
    super(null, node);
  }
}
