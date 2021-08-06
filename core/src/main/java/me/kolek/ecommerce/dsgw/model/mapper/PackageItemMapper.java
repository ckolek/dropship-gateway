package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.PackageItemDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentItem;
import me.kolek.ecommerce.dsgw.model.PackageItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {PackageMapper.class,
    OrderItemMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class PackageItemMapper {

  public static final String FIELD__PACKAGE = "package";
  public static final String FIELD__ORDER_ITEM = "orderItem";

  @Inject
  private PackageMapper packageMapper;

  @Inject
  private OrderItemMapper orderItemMapper;

  @Mapping(target = FIELD__PACKAGE, ignore = true)
  @Mapping(target = FIELD__ORDER_ITEM, ignore = true)
  public abstract PackageItemDTO packageItemToDto(PackageItem packageItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  protected abstract List<PackageItemDTO> packageItemToDtoList(Collection<PackageItem> items,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(PackageItem packageItem, @MappingTarget PackageItemDTO packageItemDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__PACKAGE, subSelection -> packageItemDTO
        .setPackage(packageMapper.packageToDto(packageItem.getPackage(), context, subSelection)));
    mapIfSelected(selection, FIELD__ORDER_ITEM, subSelection -> packageItemDTO.setOrderItem(
        orderItemMapper.orderItemToDto(packageItem.getOrderItem(), context, subSelection)));
  }

  public abstract PackageItem orderShipmentItemToPackageItem(OrderShipmentItem item);
}
