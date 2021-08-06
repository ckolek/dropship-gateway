package me.kolek.ecommerce.dsgw.test;

import static me.kolek.ecommerce.dsgw.util.OrderUtil.getOrderItemByLineNumber;

import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderRequest;

public class AcknowledgeOrder {

  public static AcknowledgeOrderRequest acceptAllLines(OrderDTO order) {
    return AcknowledgeOrderRequest.builder()
        .items(order.getItems().stream()
            .map(AcknowledgeOrder::acceptLineImplicit)
            .toList())
        .build();
  }

  public static AcknowledgeOrderItem acceptLineImplicit(int lineNumber) {
    return AcknowledgeOrderItem.builder().lineNumber(lineNumber).build();
  }

  public static AcknowledgeOrderItem acceptLineImplicit(OrderItemDTO orderItem) {
    return AcknowledgeOrderItem.builder().lineNumber(orderItem.getLineNumber()).build();
  }

  public static AcknowledgeOrderItem acceptLineExplicit(OrderDTO order, int lineNumber) {
    return getOrderItemByLineNumber(order, lineNumber).map(AcknowledgeOrder::acceptLineExplicit)
        .orElseThrow();
  }

  public static AcknowledgeOrderItem acceptLineExplicit(OrderItemDTO orderItem) {
    return AcknowledgeOrderItem.builder()
        .lineNumber(orderItem.getLineNumber())
        .quantityAccepted(orderItem.getQuantity())
        .build();
  }

  public static AcknowledgeOrderItem rejectLineImplicit(int lineNumber, String rejectCode,
      String rejectReason) {
    return AcknowledgeOrderItem.builder()
        .lineNumber(lineNumber)
        .rejectCode(rejectCode)
        .rejectReason(rejectReason)
        .build();
  }

  public static AcknowledgeOrderItem rejectLineExplicit(OrderDTO order, int lineNumber,
      String rejectCode, String rejectReason) {
    return getOrderItemByLineNumber(order, lineNumber)
        .map(orderItem -> rejectLineExplicit(orderItem, rejectCode, rejectReason))
        .orElseThrow();
  }

  public static AcknowledgeOrderItem rejectLineExplicit(OrderItemDTO orderItem,
      String rejectCode, String rejectReason) {
    return AcknowledgeOrderItem.builder()
        .lineNumber(orderItem.getLineNumber())
        .quantityRejected(orderItem.getQuantity())
        .rejectCode(rejectCode)
        .rejectReason(rejectReason)
        .build();
  }

  public static AcknowledgeOrderItem rejectLinePartial(OrderDTO order, int lineNumber,
      String rejectCode, String rejectReason) {
    return getOrderItemByLineNumber(order, lineNumber)
        .map(orderItem -> rejectLinePartial(orderItem, rejectCode, rejectReason))
        .orElseThrow();
  }

  public static AcknowledgeOrderItem rejectLinePartial(OrderItemDTO orderItem,
      String rejectCode, String rejectReason) {
    int quantityAccepted = orderItem.getQuantity() / 2;

    return AcknowledgeOrderItem.builder()
        .lineNumber(orderItem.getLineNumber())
        .quantityAccepted(quantityAccepted)
        .quantityRejected(orderItem.getQuantity() - quantityAccepted)
        .rejectCode(rejectCode)
        .rejectReason(rejectReason)
        .build();
  }
}
