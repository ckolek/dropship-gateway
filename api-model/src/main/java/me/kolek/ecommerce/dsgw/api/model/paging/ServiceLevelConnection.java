package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelDTO;

public class ServiceLevelConnection extends Connection<ServiceLevelDTO, ServiceLevelEdge> {

  @JsonCreator
  public ServiceLevelConnection(List<ServiceLevelEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
