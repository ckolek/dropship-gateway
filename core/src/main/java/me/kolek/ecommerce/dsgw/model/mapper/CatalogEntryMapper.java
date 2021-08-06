package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {CatalogMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class CatalogEntryMapper {

  public static final String FIELD__CATALOG = "catalog";
  public static final String FIELD__ITEM = "item";
  public static final String FIELD__OPTIONS = "options";

  @Inject
  private CatalogMapper catalogMapper;

  @Mapping(target = FIELD__CATALOG, ignore = true)
  @Mapping(target = FIELD__ITEM, ignore = true)
  @Mapping(target = FIELD__OPTIONS, ignore = true)
  protected abstract CatalogEntryDTO catalogItemToDto(CatalogItem catalogItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(CatalogItem catalogItem,
      @MappingTarget CatalogEntryDTO catalogEntryDTO, @Context CycleAvoidingMappingContext context,
      @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__CATALOG, subSelection -> catalogEntryDTO.setCatalog(
        catalogMapper.catalogToDto(catalogItem.getCatalog(), context, subSelection)));
    mapIfSelected(selection, FIELD__ITEM, subSelection -> catalogEntryDTO.setItem(
        catalogItemToDto(catalogItem.getParentItem(), context, subSelection)));
    mapIfSelected(selection, FIELD__OPTIONS, subSelection -> catalogEntryDTO.setOptions(
        catalogItemsToDtoList(catalogItem.getChildItems(), context, subSelection)));

    if (context.isSetParentReferences() && catalogEntryDTO.getOptions() != null) {
      catalogEntryDTO.getOptions().forEach(option -> option.setItem(catalogEntryDTO));
    }
  }

  protected abstract List<CatalogEntryDTO> catalogItemsToDtoList(
      Collection<CatalogItem> catalogItems, @Context CycleAvoidingMappingContext context,
      @Context MappingFieldSelection selection);
}
