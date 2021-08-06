package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class ServiceLevelEdge extends Edge<ServiceLevelDTO> {

  @JsonCreator
  public ServiceLevelEdge(ServiceLevelDTO node) {
    super(null, node);
  }
}
