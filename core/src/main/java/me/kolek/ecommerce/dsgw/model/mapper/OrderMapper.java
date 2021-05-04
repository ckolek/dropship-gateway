package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {UuidMapper.class, WarehouseMapper.class, ContactMapper.class, AddressMapper.class,
    OrderItemMapper.class, ServiceLevelMapper.class,
    OrderCancelCodeMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class OrderMapper {

  private static final String FIELD__WAREHOUSE = "warehouse";
  private static final String FIELD__ITEMS = "items";
  private static final String FIELD__SERVICE_LEVEL = "serviceLevel";

  @Inject
  private WarehouseMapper warehouseMapper;

  @Inject
  private ServiceLevelMapper serviceLevelMapper;

  public OrderDTO orderToDto(Order order, @Context MappingFieldSelection selection) {
    return orderToDto(order, new CycleAvoidingMappingContext(), selection);
  }

  @Mapping(source = "externalId", target = "orderNumber")
  @Mapping(target = FIELD__WAREHOUSE, ignore = true)
  @Mapping(target = FIELD__ITEMS, ignore = true)
  @Mapping(target = FIELD__SERVICE_LEVEL, ignore = true)
  abstract OrderDTO orderToDto(Order order, @Context CycleAvoidingMappingContext context,
      @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Order order, @MappingTarget OrderDTO orderDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__WAREHOUSE, subSelection -> orderDTO
        .setWarehouse(warehouseMapper.warehouseToDto(order.getWarehouse(), context, subSelection)));
    mapIfSelected(selection, FIELD__ITEMS, subSelection -> orderDTO
        .setItems(orderItemListToDtoList(order.getItems(), context, subSelection)));
    mapIfSelected(selection, FIELD__SERVICE_LEVEL, subSelection -> orderDTO.setServiceLevel(
        serviceLevelMapper.serviceLevelToDto(order.getServiceLevel(), context, subSelection)));

    if (orderDTO.getItems() != null) {
      orderDTO.getItems().forEach(item -> item.setOrder(orderDTO));
    }
  }

  protected abstract List<OrderItemDTO> orderItemListToDtoList(List<OrderItem> orderItems,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);
}
