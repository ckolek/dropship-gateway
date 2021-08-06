package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.PackageDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentRequest;
import me.kolek.ecommerce.dsgw.model.Package;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {OrderMapper.class, WarehouseMapper.class, PackageItemMapper.class,
    ContactMapper.class, AddressMapper.class,
    ServiceLevelMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class PackageMapper {

  public static final String FIELD__ORDER = "order";
  public static final String FIELD__WAREHOUSE = "warehouse";
  public static final String FIELD__ITEMS = "items";

  @Inject
  private OrderMapper orderMapper;

  @Inject
  private WarehouseMapper warehouseMapper;

  @Inject
  private PackageItemMapper packageItemMapper;

  @Mapping(source = "externalId", target = "manifestId")
  @Mapping(target = FIELD__ORDER, ignore = true)
  @Mapping(target = FIELD__WAREHOUSE, ignore = true)
  @Mapping(target = FIELD__ITEMS, ignore = true)
  public abstract PackageDTO packageToDto(Package _package,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  protected abstract List<PackageDTO> packagesToDtoList(Collection<Package> packages,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Package _package, @MappingTarget PackageDTO packageDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__ORDER, subSelection -> packageDTO.setOrder(
        orderMapper.orderToDto(_package.getOrder(), context, subSelection)));
    mapIfSelected(selection, FIELD__WAREHOUSE, subSelection -> packageDTO.setWarehouse(
        warehouseMapper.warehouseToDto(_package.getWarehouse(), context, subSelection)));
    mapIfSelected(selection, FIELD__ITEMS, subSelection -> packageDTO.setItems(
        packageItemMapper.packageItemToDtoList(_package.getItems(), context, subSelection)));

    if (context.isSetParentReferences() && packageDTO.getItems() != null) {
      packageDTO.getItems().forEach(item -> item.setPackage(packageDTO));
    }
  }

  @Mapping(source = "manifestId", target = "externalId")
  @Mapping(source = "sender.contact", target = "senderContact")
  @Mapping(source = "sender.address", target = "senderAddress")
  @Mapping(source = "recipient.contact", target = "recipientContact")
  @Mapping(source = "recipient.address", target = "recipientAddress")
  @Mapping(target = "items", ignore = true)
  public abstract Package orderShipmentRequestToPackage(OrderShipmentRequest request);
}
