package me.kolek.ecommerce.dsgw.test;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRecipient;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;

public class SubmitOrder {
  public static SubmitOrderRequest validRequest(int itemCount) {
    String customerOrderNumber = Long.toString(System.currentTimeMillis() % 1000000);

    return SubmitOrderRequest.builder()
        .orderNumber(customerOrderNumber + "-1")
        .customerOrderNumber(customerOrderNumber)
        .warehouseCode("CWK1")
        .recipient(SubmitOrderRecipient.builder()
            .contact(ContactDTO.builder()
                .name("Chris Kolek")
                .email("ckolek@gmail.com")
                .phone("978-846-4525")
                .build())
            .address(AddressDTO.builder()
                .line1("100 Heard Street")
                .line2("Unit 314")
                .city("Chelsea")
                .state("MA")
                .postalCode("02150")
                .country("USA")
                .build())
            .build())
        .items(IntStream.range(0, itemCount)
            .mapToObj(i -> SubmitOrderItem.builder()
                .sku("CK-" + (1000 + i + 1))
                .quantity(10 + i)
                .build())
            .toList())
        .carrierName("FedEx")
        .carrierMode("Home Delivery")
        .timeOrdered(OffsetDateTime.now().minusHours(6))
        .timeReleased(OffsetDateTime.now())
        .build();
  }

  public static SubmitOrderRequest validRequest() {
    return validRequest(2);
  }

  public static SubmitOrderRequest fromOrder(OrderDTO order) {
    return SubmitOrderRequest.builder()
        .orderNumber(order.getOrderNumber())
        .customerOrderNumber(order.getCustomerOrderNumber())
        .warehouseCode(order.getWarehouse().getCode())
        .recipient(SubmitOrderRecipient.builder()
            .contact(order.getRecipient().getContact())
            .address(order.getRecipient().getAddress())
            .build())
        .items(order.getItems().stream().map(SubmitOrder::fromOrderItem).toList())
        .carrierName(order.getServiceLevel().getCarrier().getName())
        .carrierMode(order.getServiceLevel().getMode())
        .timeOrdered(order.getTimeOrdered())
        .timeReleased(order.getTimeReleased())
        .build();
  }

  private static SubmitOrderItem fromOrderItem(OrderItemDTO orderItem) {
    return SubmitOrderItem.builder()
        .sku(orderItem.getCatalogEntry().getSku())
        .gtin(orderItem.getCatalogEntry().getSku())
        .upc(orderItem.getCatalogEntry().getSku())
        .ean(orderItem.getCatalogEntry().getSku())
        .isbn(orderItem.getCatalogEntry().getSku())
        .quantity(orderItem.getQuantity())
        .customization(orderItem.getCustomization())
        .expectedShipDate(orderItem.getExpectedShipDate())
        .expectedDeliveryDate(orderItem.getExpectedDeliveryDate())
        .build();
  }
}
