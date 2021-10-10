package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.model.Catalog;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {CatalogEntryMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class CatalogMapper {

  public static final String FIELD__SUPPLIER = "supplier";
  public static final String FIELD__ITEMS = "items";

  @Inject
  private SupplierMapper supplierMapper;

  @Inject
  private CatalogEntryMapper catalogEntryMapper;

  public CatalogDTO catalogToDto(Catalog catalog, @Context MappingFieldSelection selection) {
    return catalogToDto(catalog, new CycleAvoidingMappingContext(), selection);
  }

  public CatalogDTO catalogToDto(Catalog catalog, boolean setParentReferences,
      @Context MappingFieldSelection selection) {
    return catalogToDto(catalog, new CycleAvoidingMappingContext(setParentReferences), selection);
  }

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
        .setItems(catalogEntryMapper.catalogItemsToDtoList(catalog.getItems(), context, subSelection)));

    if (context.isSetParentReferences() && catalogDTO.getItems() != null) {
      catalogDTO.getItems().forEach(item -> item.setCatalog(catalogDTO));
    }
  }
}
