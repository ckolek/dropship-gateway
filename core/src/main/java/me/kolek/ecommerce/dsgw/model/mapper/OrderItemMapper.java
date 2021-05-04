package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {UuidMapper.class, OrderMapper.class, CatalogEntryMapper.class, ContactMapper.class,
    AddressMapper.class, OrderCancelCodeMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class OrderItemMapper {

  private static final String FIELD__ORDER = "order";
  private static final String FIELD__CATALOG_ENTRY = "catalogEntry";

  @Inject
  private OrderMapper orderMapper;

  @Inject
  private CatalogEntryMapper catalogEntryMapper;

  @Mapping(target = FIELD__ORDER, ignore = true)
  @Mapping(target = FIELD__CATALOG_ENTRY, ignore = true)
  abstract OrderItemDTO orderItemToDto(OrderItem orderItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(OrderItem orderItem, @MappingTarget OrderItemDTO orderItemDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__ORDER, subSelection -> orderItemDTO
        .setOrder(orderMapper.orderToDto(orderItem.getOrder(), context, subSelection)));
    mapIfSelected(selection, FIELD__CATALOG_ENTRY, subSelection -> orderItemDTO.setCatalogEntry(
        catalogEntryMapper
            .catalogItemToEntryDto(orderItem.getCatalogItem(), context, subSelection)));
  }
}
