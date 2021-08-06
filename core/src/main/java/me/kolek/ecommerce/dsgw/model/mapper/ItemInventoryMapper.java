package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.InventoryDTO;
import me.kolek.ecommerce.dsgw.model.ItemInventory;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {CatalogEntryMapper.class,
    WarehouseMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class ItemInventoryMapper {

  public static final String FIELD__CATALOG_ENTRY = "catalogEntry";
  public static final String FIELD__WAREHOUSE = "warehouse";

  @Inject
  private CatalogEntryMapper catalogEntryMapper;

  @Inject
  private WarehouseMapper warehouseMapper;

  @Mapping(target = FIELD__CATALOG_ENTRY, ignore = true)
  @Mapping(target = FIELD__WAREHOUSE, ignore = true)
  abstract InventoryDTO itemInventoryToDto(ItemInventory itemInventory,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(ItemInventory itemInventory, @MappingTarget InventoryDTO inventoryDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__CATALOG_ENTRY, subSelection -> inventoryDTO.setCatalogEntry(
        catalogEntryMapper
            .catalogItemToDto(itemInventory.getCatalogItem(), context, subSelection)));
    mapIfSelected(selection, FIELD__WAREHOUSE, subSelection -> inventoryDTO.setWarehouse(
        warehouseMapper.warehouseToDto(itemInventory.getWarehouse(), context, subSelection)));
  }
}
