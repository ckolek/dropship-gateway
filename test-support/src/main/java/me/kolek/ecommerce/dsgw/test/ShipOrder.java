package me.kolek.ecommerce.dsgw.test;

import java.time.OffsetDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentCorrespondent;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentRequest;
import me.kolek.ecommerce.dsgw.util.OrderUtil;

public class ShipOrder {

  public static final String TRACKING_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  public static String generateTrackingNumber() {
    return generateTrackingNumber(16);
  }

  public static String generateTrackingNumber(int length) {
    Random random = ThreadLocalRandom.current();

    char[] chars = new char[length];
    for (int i = 0; i < length; i++) {
      chars[i] = TRACKING_CHARS.charAt(random.nextInt(TRACKING_CHARS.length()));
    }
    return new String(chars);
  }

  public static OrderShipmentRequest shipPartOfOrder(OrderDTO order) {
    return OrderShipmentRequest.builder()
        .sender(OrderShipmentCorrespondent.builder()
            .contact(ContactDTO.builder()
                .name("Some Vendor")
                .email("some1@vendor.com")
                .phone("508-890-2134")
                .build())
            .address(order.getWarehouse().getAddress())
            .build())
        .recipient(OrderShipmentCorrespondent.builder()
            .contact(order.getRecipient().getContact())
            .address(order.getRecipient().getAddress())
            .build())
        .items(order.getItems().stream()
            .filter(OrderUtil::hasQuantityRemaining)
            .limit(1)
            .map(orderItem -> OrderShipmentItem.builder()
                .orderLineNumber(orderItem.getLineNumber())
                .quantity(1)
                .build())
            .toList())
        .warehouseCode(order.getWarehouse().getSupplierCode())
        .carrierServiceLevelCode(order.getServiceLevel().getCode())
        .trackingNumber(generateTrackingNumber())
        .timeShipped(OffsetDateTime.now())
        .build();
  }

  public static OrderShipmentRequest shipRemainderOfOrder(OrderDTO order) {
    return OrderShipmentRequest.builder()
        .sender(OrderShipmentCorrespondent.builder()
            .contact(ContactDTO.builder()
                .name("Some Vendor")
                .email("some1@vendor.com")
                .phone("508-890-2134")
                .build())
            .address(order.getWarehouse().getAddress())
            .build())
        .recipient(OrderShipmentCorrespondent.builder()
            .contact(order.getRecipient().getContact())
            .address(order.getRecipient().getAddress())
            .build())
        .items(order.getItems().stream()
            .filter(OrderUtil::hasQuantityRemaining)
            .limit(1)
            .map(orderItem -> OrderShipmentItem.builder()
                .orderLineNumber(orderItem.getLineNumber())
                .quantity(OrderUtil.getQuantityRemaining(orderItem))
                .build())
            .toList())
        .warehouseCode(order.getWarehouse().getSupplierCode())
        .carrierServiceLevelCode(order.getServiceLevel().getCode())
        .trackingNumber(generateTrackingNumber())
        .timeShipped(OffsetDateTime.now())
        .build();
  }

  public static OrderShipmentRequest shipEntireOrder(OrderDTO order) {
    return OrderShipmentRequest.builder()
        .sender(OrderShipmentCorrespondent.builder()
            .contact(ContactDTO.builder()
                .name("Some Vendor")
                .email("some1@vendor.com")
                .phone("508-890-2134")
                .build())
            .address(order.getWarehouse().getAddress())
            .build())
        .recipient(OrderShipmentCorrespondent.builder()
            .contact(order.getRecipient().getContact())
            .address(order.getRecipient().getAddress())
            .build())
        .items(order.getItems().stream()
            .map(orderItem -> OrderShipmentItem.builder()
                .orderLineNumber(orderItem.getLineNumber())
                .quantity(OrderUtil.getQuantityExpected(orderItem))
                .build())
            .toList())
        .warehouseCode(order.getWarehouse().getSupplierCode())
        .carrierServiceLevelCode(order.getServiceLevel().getCode())
        .trackingNumber(generateTrackingNumber())
        .timeShipped(OffsetDateTime.now())
        .build();
  }
}
