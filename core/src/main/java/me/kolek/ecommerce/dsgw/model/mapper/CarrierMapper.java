package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import java.util.Set;
import me.kolek.ecommerce.dsgw.api.model.CarrierDTO;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelDTO;
import me.kolek.ecommerce.dsgw.model.Carrier;
import me.kolek.ecommerce.dsgw.model.ServiceLevel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {UuidMapper.class,
    ServiceLevelMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class CarrierMapper {

  private static final String FIELD__SERVICE_LEVELS = "serviceLevels";

  @Mapping(target = FIELD__SERVICE_LEVELS, ignore = true)
  public abstract CarrierDTO carrierToDto(Carrier carrier,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Carrier carrier, @MappingTarget CarrierDTO carrierDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__SERVICE_LEVELS, subSelection -> carrierDTO.setServiceLevels(
        serviceLevelSetToDtoList(carrier.getServiceLevels(), context, subSelection)));

    if (carrierDTO.getServiceLevels() != null) {
      carrierDTO.getServiceLevels().forEach(serviceLevel -> serviceLevel.setCarrier(carrierDTO));
    }
  }

  protected abstract List<ServiceLevelDTO> serviceLevelSetToDtoList(Set<ServiceLevel> serviceLevels,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);
}
