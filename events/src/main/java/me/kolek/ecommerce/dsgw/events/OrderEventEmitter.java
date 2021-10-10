package me.kolek.ecommerce.dsgw.events;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.api.model.event.Metadata;
import me.kolek.ecommerce.dsgw.api.model.event.OrderEventDTO;
import me.kolek.ecommerce.dsgw.context.RequestContext;
import me.kolek.ecommerce.dsgw.events.config.EventProperties;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.mapper.CatalogEntryMapper;
import me.kolek.ecommerce.dsgw.model.mapper.InvoiceItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.InvoiceMapper;
import me.kolek.ecommerce.dsgw.model.mapper.MappingFieldSelection;
import me.kolek.ecommerce.dsgw.model.mapper.OrderItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.OrderMapper;
import me.kolek.ecommerce.dsgw.model.mapper.PackageItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.PackageMapper;
import me.kolek.ecommerce.dsgw.model.mapper.ServiceLevelMapper;
import me.kolek.ecommerce.dsgw.model.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventEmitter extends BaseEventEmitter {

  private final OrderMapper orderMapper;
  private final EventProperties eventProperties;

  private final MappingFieldSelection MAPPING_FIELD_SELECTION = MappingFieldSelection.builder()
      .field(OrderMapper.FIELD__WAREHOUSE, warehouse ->
          warehouse.field(WarehouseMapper.FIELD__SUPPLIER))
      .field(OrderMapper.FIELD__ITEMS, items ->
          items.field(OrderItemMapper.FIELD__CATALOG_ENTRY, catalogEntry ->
              catalogEntry.field(CatalogEntryMapper.FIELD__CATALOG)
                  .field(CatalogEntryMapper.FIELD__ITEM, item ->
                      item.field(CatalogEntryMapper.FIELD__CATALOG))))
      .field(OrderMapper.FIELD__SERVICE_LEVEL, serviceLevel ->
          serviceLevel.field(ServiceLevelMapper.FIELD__CARRIER))
      .field(OrderMapper.FIELD__PACKAGES, packages ->
          packages.field(PackageMapper.FIELD__ITEMS, items ->
              items.field(PackageItemMapper.FIELD__ORDER_ITEM)))
      .field(OrderMapper.FIELD__INVOICES, invoices ->
          invoices.field(InvoiceMapper.FIELD__ITEMS, items ->
              items.field(InvoiceItemMapper.FIELD__ORDER_ITEM)))
      .build();

  @Inject
  public OrderEventEmitter(ObjectMapper objectMapper,
      AmazonSNS sns,
      OrderMapper orderMapper,
      EventProperties eventProperties) {
    super(objectMapper, sns);
    this.orderMapper = orderMapper;
    this.eventProperties = eventProperties;
  }

  public void emitEvent(Order order, OrderEventDTO.Type type) throws Exception {
    emitEvent(order, type, null);
  }

  public void emitEvent(Order order, OrderEventDTO.Type type, OrderEventDTO.SubType subType)
      throws Exception {
    var event = OrderEventDTO.builder()
        .metadata(Metadata.builder()
            .requestId(RequestContext.get().getId())
            .recordId(order.getId().toString())
            .recordVersion(order.getRecordVersion().longValue())
            .timeEmitted(OffsetDateTime.now())
            .build())
        .type(type)
        .subType(subType)
        .record(orderMapper.orderToDto(order, false, MAPPING_FIELD_SELECTION))
        .build();

    emitEvent(event, eventProperties.getOrderEventTopicArn());
  }
}
