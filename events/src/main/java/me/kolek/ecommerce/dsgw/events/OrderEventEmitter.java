package me.kolek.ecommerce.dsgw.events;

import static me.kolek.ecommerce.dsgw.aws.sns.AwsSnsUtil.toMessageAttributeValueMap;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Metadata;
import me.kolek.ecommerce.dsgw.context.RequestContext;
import me.kolek.ecommerce.dsgw.events.config.EventProperties;
import me.kolek.ecommerce.dsgw.internal.model.queue.MessageAttributes;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.mapper.CatalogEntryMapper;
import me.kolek.ecommerce.dsgw.model.mapper.MappingFieldSelection;
import me.kolek.ecommerce.dsgw.model.mapper.OrderItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.OrderMapper;
import me.kolek.ecommerce.dsgw.model.mapper.ServiceLevelMapper;
import me.kolek.ecommerce.dsgw.model.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class OrderEventEmitter {

  private final OrderMapper orderMapper;
  private final ObjectMapper objectMapper;
  private final EventProperties eventProperties;
  private final AmazonSNS sns;

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
      .build();

  public void emitEvent(Order order, OrderEventDTO.Type type) throws Exception {
    emitEvent(order, type, null);
  }

  public void emitEvent(Order order, OrderEventDTO.Type type, OrderEventDTO.SubType subType)
      throws Exception {
    var event = OrderEventDTO.builder()
        .metadata(Metadata.builder()
            .requestId(RequestContext.get().getId())
            .orderId(order.getId().toString())
            .orderVersion(order.getRecordVersion().longValue())
            .timeEmitted(OffsetDateTime.now())
            .build())
        .type(type)
        .subType(subType)
        .order(orderMapper.orderToDto(order, false, MAPPING_FIELD_SELECTION))
        .build();

    String message = objectMapper.writeValueAsString(event);

    Map<String, String> messageAttributes = new HashMap<>();
    messageAttributes.put(MessageAttributes.EVENT_TYPE, type.toString());
    if (subType != null) {
        messageAttributes.put(MessageAttributes.EVENT_SUB_TYPE, subType.toString());
    }

    sns.publish(new PublishRequest()
        .withTopicArn(eventProperties.getOrderEventTopicArn())
        .withMessageAttributes(toMessageAttributeValueMap(messageAttributes))
        .withMessage(message));
  }
}
