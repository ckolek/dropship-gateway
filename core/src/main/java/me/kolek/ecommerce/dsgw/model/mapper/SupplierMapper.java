package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import java.util.Set;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO;
import me.kolek.ecommerce.dsgw.model.Supplier;
import me.kolek.ecommerce.dsgw.model.Warehouse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {UuidMapper.class, WarehouseMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class SupplierMapper {

  private final String FIELD__WAREHOUSES = "warehouses";

  @Mapping(target = FIELD__WAREHOUSES, ignore = true)
  protected abstract SupplierDTO supplierToDto(Supplier supplier,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Supplier supplier, @MappingTarget SupplierDTO supplierDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__WAREHOUSES, subSelection -> supplierDTO
        .setWarehouses(warehouseSetToDtoList(supplier.getWarehouses(), context, subSelection)));

    if (supplierDTO.getWarehouses() != null) {
      supplierDTO.getWarehouses().forEach(warehouse -> warehouse.setSupplier(supplierDTO));
    }
  }

  protected abstract List<WarehouseDTO> warehouseSetToDtoList(Set<Warehouse> warehouse,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);
}
