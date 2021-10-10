package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.model.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {WarehouseMapper.class, ContactMapper.class, AddressMapper.class,
    OrderItemMapper.class, ServiceLevelMapper.class, OrderCancelCodeMapper.class,
    PackageMapper.class, InvoiceMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class OrderMapper {

  public static final String FIELD__WAREHOUSE = "warehouse";
  public static final String FIELD__ITEMS = "items";
  public static final String FIELD__SERVICE_LEVEL = "serviceLevel";
  public static final String FIELD__PACKAGES = "packages";
  public static final String FIELD__INVOICES = "invoices";

  @Inject
  private WarehouseMapper warehouseMapper;

  @Inject
  private ServiceLevelMapper serviceLevelMapper;

  @Inject
  private OrderItemMapper orderItemMapper;

  @Inject
  private PackageMapper packageMapper;

  @Inject
  private InvoiceMapper invoiceMapper;

  public OrderDTO orderToDto(Order order) {
    return orderToDto(order, MappingFieldSelection.ALL);
  }

  public OrderDTO orderToDto(Order order, @Context MappingFieldSelection selection) {
    return orderToDto(order, new CycleAvoidingMappingContext(), selection);
  }

  public OrderDTO orderToDto(Order order, boolean setParentReferences,
      @Context MappingFieldSelection selection) {
    return orderToDto(order, new CycleAvoidingMappingContext(setParentReferences), selection);
  }

  @Mapping(source = "externalId", target = "orderNumber")
  @Mapping(target = FIELD__WAREHOUSE, ignore = true)
  @Mapping(source = "contact", target = "recipient.contact")
  @Mapping(source = "address", target = "recipient.address")
  @Mapping(target = FIELD__ITEMS, ignore = true)
  @Mapping(target = FIELD__SERVICE_LEVEL, ignore = true)
  @Mapping(target = FIELD__PACKAGES, ignore = true)
  @Mapping(target = FIELD__INVOICES, ignore = true)
  abstract OrderDTO orderToDto(Order order, @Context CycleAvoidingMappingContext context,
      @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Order order, @MappingTarget OrderDTO orderDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__WAREHOUSE, subSelection -> orderDTO
        .setWarehouse(warehouseMapper.warehouseToDto(order.getWarehouse(), context, subSelection)));
    mapIfSelected(selection, FIELD__ITEMS, subSelection -> orderDTO
        .setItems(orderItemMapper.orderItemsToDtoList(order.getItems(), context, subSelection)));
    mapIfSelected(selection, FIELD__SERVICE_LEVEL, subSelection -> orderDTO.setServiceLevel(
        serviceLevelMapper.serviceLevelToDto(order.getServiceLevel(), context, subSelection)));
    mapIfSelected(selection, FIELD__PACKAGES, subSelection -> orderDTO
        .setPackages(packageMapper.packagesToDtoList(order.getPackages(), context, subSelection)));
    mapIfSelected(selection, FIELD__INVOICES, subSelection -> orderDTO
        .setInvoices(invoiceMapper.invoicesToDtoList(order.getInvoices(), context, subSelection)));

    if (context.isSetParentReferences() && orderDTO.getItems() != null) {
      orderDTO.getItems().forEach(item -> item.setOrder(orderDTO));
    }
    if (context.isSetParentReferences() && orderDTO.getPackages() != null) {
      orderDTO.getPackages().forEach(_package -> _package.setOrder(orderDTO));
    }
    if (context.isSetParentReferences() && orderDTO.getInvoices() != null) {
      orderDTO.getInvoices().forEach(invoice -> invoice.setOrder(orderDTO));
    }
  }

  @Mapping(source = "orderNumber", target = "externalId")
  @Mapping(source = "recipient.contact", target = "contact")
  @Mapping(source = "recipient.address", target = "address")
  @Mapping(target = "items", ignore = true)
  @Mapping(target = "status", constant = "NEW")
  public abstract Order submitOrderRequestToOrder(SubmitOrderRequest request, @MappingTarget Order order);
}
