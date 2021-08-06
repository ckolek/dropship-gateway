package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderItem;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {OrderMapper.class, CatalogEntryMapper.class, ContactMapper.class,
    AddressMapper.class, OrderCancelCodeMapper.class,
    PackageItemMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class OrderItemMapper {

  public static final String FIELD__ORDER = "order";
  public static final String FIELD__CATALOG_ENTRY = "catalogEntry";
  public static final String FIELD__PACKAGE_ITEMS = "packageItems";

  @Inject
  private OrderMapper orderMapper;

  @Inject
  private CatalogEntryMapper catalogEntryMapper;

  @Inject
  private PackageItemMapper packageItemMapper;

  @Mapping(target = FIELD__ORDER, ignore = true)
  @Mapping(target = FIELD__CATALOG_ENTRY, ignore = true)
  @Mapping(target = FIELD__PACKAGE_ITEMS, ignore = true)
  abstract OrderItemDTO orderItemToDto(OrderItem orderItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  protected abstract List<OrderItemDTO> orderItemsToDtoList(Collection<OrderItem> orderItems,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(OrderItem orderItem, @MappingTarget OrderItemDTO orderItemDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__ORDER, subSelection -> orderItemDTO.setOrder(
        orderMapper.orderToDto(orderItem.getOrder(), context, subSelection)));
    mapIfSelected(selection, FIELD__CATALOG_ENTRY, subSelection -> orderItemDTO.setCatalogEntry(
        catalogEntryMapper.catalogItemToDto(orderItem.getCatalogItem(), context, subSelection)));
    mapIfSelected(selection, FIELD__PACKAGE_ITEMS, subSelection -> orderItemDTO.setPackageItems(
        packageItemMapper.packageItemToDtoList(orderItem.getPackageItems(), context, subSelection)));
  }

  @Mapping(target = "status", constant = "NEW")
  public abstract OrderItem submitOrderItemToOrderItem(SubmitOrderItem submitOrderItem);
}
