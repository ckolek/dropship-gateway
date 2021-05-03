package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelDTO;
import me.kolek.ecommerce.dsgw.model.ServiceLevel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UuidMapper.class, CarrierMapper.class})
public abstract class ServiceLevelMapper {

  private final String FIELD__CARRIER = "carrier";

  @Inject
  private CarrierMapper carrierMapper;

  @Mapping(target = FIELD__CARRIER, ignore = true)
  abstract ServiceLevelDTO serviceLevelToDto(ServiceLevel serviceLevel,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(ServiceLevel serviceLevel, ServiceLevelDTO serviceLevelDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__CARRIER, subSelection -> serviceLevelDTO
        .setCarrier(carrierMapper.carrierToDto(serviceLevel.getCarrier(), context, subSelection)));
  }
}
