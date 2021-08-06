package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.CarrierDTO;
import me.kolek.ecommerce.dsgw.model.Carrier;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {ServiceLevelMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class CarrierMapper {

  public static final String FIELD__SERVICE_LEVELS = "serviceLevels";

  @Inject
  private ServiceLevelMapper serviceLevelMapper;

  @Mapping(target = FIELD__SERVICE_LEVELS, ignore = true)
  public abstract CarrierDTO carrierToDto(Carrier carrier,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Carrier carrier, @MappingTarget CarrierDTO carrierDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__SERVICE_LEVELS, subSelection -> carrierDTO.setServiceLevels(
        serviceLevelMapper.serviceLevelsToDtoList(carrier.getServiceLevels(), context, subSelection)));

    if (context.isSetParentReferences() && carrierDTO.getServiceLevels() != null) {
      carrierDTO.getServiceLevels().forEach(serviceLevel -> serviceLevel.setCarrier(carrierDTO));
    }
  }
}
