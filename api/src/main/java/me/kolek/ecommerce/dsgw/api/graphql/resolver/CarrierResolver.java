package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import me.kolek.ecommerce.dsgw.api.model.CarrierDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.ServiceLevelConnection;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelDTO;
import org.springframework.stereotype.Component;

@Component
public class CarrierResolver implements GraphQLResolver<CarrierDTO> {
  public ServiceLevelDTO serviceLevel(CarrierDTO carrierDTO, String name) {
    return null;
  }

  public ServiceLevelConnection serviceLevels(CarrierDTO carrierDTO, int pageSize, int pageOffset) {
    return null;
  }
}
