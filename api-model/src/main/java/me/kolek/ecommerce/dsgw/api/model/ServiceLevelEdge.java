package me.kolek.ecommerce.dsgw.api.model;

import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Edge;

public class ServiceLevelEdge extends Edge<ServiceLevelDTO> {

  public ServiceLevelEdge(ServiceLevelDTO node) {
    super(null, node);
  }
}
