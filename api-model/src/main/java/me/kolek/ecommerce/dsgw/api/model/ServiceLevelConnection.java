package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.PageInfo;

public class ServiceLevelConnection extends Connection<ServiceLevelDTO, ServiceLevelEdge> {

  public ServiceLevelConnection(List<ServiceLevelEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
