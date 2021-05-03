package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO;
import me.kolek.ecommerce.dsgw.model.Warehouse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {UuidMapper.class, SupplierMapper.class})
public abstract class WarehouseMapper {

  private static final String FIELD__SUPPLIER = "supplier";

  @Inject
  private SupplierMapper supplierMapper;

  @Mapping(target = FIELD__SUPPLIER, ignore = true)
  abstract WarehouseDTO warehouseToDto(Warehouse warehouse,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Warehouse warehouse, @MappingTarget WarehouseDTO warehouseDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__SUPPLIER, subSelection -> warehouseDTO
        .setSupplier(supplierMapper.supplierToDto(warehouse.getSupplier(), context, subSelection)));
  }
}
