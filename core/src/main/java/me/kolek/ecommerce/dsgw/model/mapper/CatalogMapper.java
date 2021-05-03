package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogItemDTO;
import me.kolek.ecommerce.dsgw.model.Catalog;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {UuidMapper.class, CatalogEntryMapper.class})
public abstract class CatalogMapper {

  private static final String FIELD__SUPPLIER = "supplier";
  private static final String FIELD__ITEMS = "items";

  @Inject
  private SupplierMapper supplierMapper;

  @Mapping(target = FIELD__SUPPLIER, ignore = true)
  @Mapping(target = FIELD__ITEMS, ignore = true)
  abstract CatalogDTO catalogToDto(Catalog catalog, @Context CycleAvoidingMappingContext context,
      @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Catalog catalog, @MappingTarget CatalogDTO catalogDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__SUPPLIER, subSelection -> catalogDTO
        .setSupplier(supplierMapper.supplierToDto(catalog.getSupplier(), context, subSelection)));
    mapIfSelected(selection, FIELD__ITEMS, subSelection -> catalogDTO
        .setItems(catalogItemListToDtoList(catalog.getItems(), context, subSelection)));
  }

  protected abstract List<CatalogItemDTO> catalogItemListToDtoList(List<CatalogItem> catalogItems,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);
}
