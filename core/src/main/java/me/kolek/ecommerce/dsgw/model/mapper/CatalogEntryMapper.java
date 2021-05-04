package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogItemDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogItemOptionDTO;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {UuidMapper.class, CatalogMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class CatalogEntryMapper {

  private static final String FIELD__CATALOG = "catalog";
  private static final String FIELD__ITEM = "item";
  private static final String FIELD__OPTIONS = "options";

  @Inject
  private CatalogMapper catalogMapper;

  CatalogEntryDTO catalogItemToEntryDto(CatalogItem catalogItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    if (catalogItem.getParentItem() == null) {
      return catalogItemToDto(catalogItem, context, selection);
    } else {
      var catalogItemOptionDTO = catalogItemToOptionDto(catalogItem, context, selection);
      catalogItemOptionDTO
          .setItem(catalogItemToDto(catalogItem.getParentItem(), context, selection));
      return catalogItemOptionDTO;
    }
  }

  @Mapping(target = FIELD__CATALOG, ignore = true)
  @Mapping(target = FIELD__OPTIONS, ignore = true)
  protected abstract CatalogItemDTO catalogItemToDto(CatalogItem catalogItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(CatalogItem catalogItem, @MappingTarget CatalogItemDTO catalogItemDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__CATALOG, subSelection -> catalogItemDTO
        .setCatalog(catalogMapper.catalogToDto(catalogItem.getCatalog(), context, subSelection)));
    mapIfSelected(selection, FIELD__OPTIONS, subSelection -> catalogItemDTO.setOptions(
        catalogItemListToOptionDTO(catalogItem.getChildItems(), context, subSelection)));

    if (catalogItemDTO.getOptions() != null) {
      catalogItemDTO.getOptions().forEach(option -> option.setItem(catalogItemDTO));
    }
  }

  protected abstract List<CatalogItemOptionDTO> catalogItemListToOptionDTO(
      List<CatalogItem> catalogItems, @Context CycleAvoidingMappingContext context,
      @Context MappingFieldSelection selection);

  @Mapping(target = FIELD__ITEM, ignore = true)
  protected abstract CatalogItemOptionDTO catalogItemToOptionDto(CatalogItem catalogItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(CatalogItem catalogItem,
      @MappingTarget CatalogItemOptionDTO catalogItemOptionDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__ITEM, subSelection -> catalogItemOptionDTO
        .setItem(catalogItemToDto(catalogItem.getParentItem(), context, subSelection)));
  }
}
