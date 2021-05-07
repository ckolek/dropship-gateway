package me.kolek.ecommerce.dsgw.worker.processor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import lombok.SneakyThrows;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.OrderActionResultBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AcknowledgeOrderAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.Order.Status;
import me.kolek.ecommerce.dsgw.model.OrderCancelCode;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import me.kolek.ecommerce.dsgw.repository.OrderCancelCodeRepository;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import me.kolek.ecommerce.dsgw.util.OrderUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class AcknowledgeOrderActionProcessor extends
    BaseOrderActionProcessor<AcknowledgeOrderAction> {

  private final OrderCancelCodeRepository orderCancelCodeRepository;
  private final OrderEventEmitter eventEmitter;

  @Inject
  public AcknowledgeOrderActionProcessor(OrderRepository orderRepository,
      OrderCancelCodeRepository orderCancelCodeRepository, OrderEventEmitter eventEmitter) {
    super(orderRepository);
    this.orderCancelCodeRepository = orderCancelCodeRepository;
    this.eventEmitter = eventEmitter;
  }

  @Override
  public String getActionType() {
    return AcknowledgeOrderAction.TYPE;
  }

  @Override
  protected void process(AcknowledgeOrderAction action, Order order,
      OrderActionResultBuilder result) {
    AcknowledgeOrderRequest request = action.getRequest();

    if (order.getStatus() == Status.ACKNOWLEDGED) {
      fail(result, "order has already been acknowledged");
      return;
    }
    if (order.getStatus() != Status.NEW) {
      fail(result, "order with status " + order.getStatus() + " cannot be acknowledged");
      return;
    }

    OffsetDateTime timeAcknowledged = OffsetDateTime.now();

    acknowledgeOrder(order, timeAcknowledged);
    acknowledgeItems(order, request.getItems(), timeAcknowledged, result);
  }

  @Override
  @SneakyThrows
  protected Order processSuccessful(AcknowledgeOrderAction action, Order order) {
    eventEmitter.emitEvent(order, Type.ORDER_ACKNOWLEDGED);

    return order;
  }

  private void acknowledgeOrder(Order order, OffsetDateTime timeAcknowledged) {
    order.setStatus(Status.ACKNOWLEDGED);
    order.setTimeAcknowledged(timeAcknowledged);
  }

  private void acknowledgeItems(Order order, List<AcknowledgeOrderItem> acknowledgeOrderItems,
      OffsetDateTime timeAcknowledged, OrderActionResult.OrderActionResultBuilder result) {
    Map<Integer, OrderItem> orderItemsByLineNumber = OrderUtil.mapOrderItemsByLineNumber(order);

    for (AcknowledgeOrderItem acknowledgeOrderItem : acknowledgeOrderItems) {
      Integer lineNumber = acknowledgeOrderItem.getLineNumber();
      OrderItem orderItem = orderItemsByLineNumber.get(lineNumber);
      if (orderItem == null) {
        fail(result, "order item with line number " + lineNumber + " not found");
        return;
      }
      acknowledgeItem(orderItem, acknowledgeOrderItem, timeAcknowledged, result);
    }
  }

  private void acknowledgeItem(OrderItem orderItem, AcknowledgeOrderItem acknowledgeOrderItem,
      OffsetDateTime timeAcknowledged, OrderActionResult.OrderActionResultBuilder result) {
    var quantityAcceptedRejected = getQuantitiesAcceptedRejected(orderItem, acknowledgeOrderItem);

    orderItem.setStatus(Status.ACKNOWLEDGED);
    orderItem.setQuantityAccepted(quantityAcceptedRejected.getFirst());
    orderItem.setQuantityRejected(quantityAcceptedRejected.getSecond());
    orderItem.setTimeAcknowledged(timeAcknowledged);

    if (!validateAcknowledgedQuantities(orderItem, result)) {
      return;
    }

    if (orderItem.getQuantityRejected() > 0) {
      findRejectCode(acknowledgeOrderItem.getRejectCode(), result).ifPresent(rejectCode -> {
        orderItem.setRejectCode(rejectCode);
        orderItem.setRejectReason(acknowledgeOrderItem.getRejectReason());
      });
    }
  }

  private static Pair<Integer, Integer> getQuantitiesAcceptedRejected(OrderItem orderItem,
      AcknowledgeOrderItem acknowledgeOrderItem) {
    int quantityAccepted, quantityRejected;
    if (acknowledgeOrderItem.getQuantityAccepted() != null
        && acknowledgeOrderItem.getQuantityRejected() != null) {
      quantityAccepted = acknowledgeOrderItem.getQuantityAccepted();
      quantityRejected = acknowledgeOrderItem.getQuantityRejected();
    } else if (acknowledgeOrderItem.getQuantityAccepted() != null) {
      quantityAccepted = acknowledgeOrderItem.getQuantityAccepted();
      quantityRejected = orderItem.getQuantity() - quantityAccepted;
    } else if (acknowledgeOrderItem.getQuantityRejected() != null) {
      quantityRejected = acknowledgeOrderItem.getQuantityRejected();
      quantityAccepted = orderItem.getQuantity() - quantityRejected;
    } else {
      quantityAccepted = orderItem.getQuantity();
      quantityRejected = 0;
    }
    return Pair.of(quantityAccepted, quantityRejected);
  }

  private static boolean validateAcknowledgedQuantities(OrderItem orderItem,
      OrderActionResult.OrderActionResultBuilder result) {
    if (orderItem.getQuantityAccepted() + orderItem.getQuantityRejected() != orderItem
        .getQuantity()) {
      fail(result,
          "quantity accepted (" + orderItem.getQuantityAccepted() + ") and quantity rejected ("
              + orderItem.getQuantityRejected() + ") must be equal to quantity ordered ("
              + orderItem.getQuantity() + ")");
      return false;
    }
    return true;
  }

  private Optional<OrderCancelCode> findRejectCode(String rejectCode,
      OrderActionResult.OrderActionResultBuilder result) {
    if (rejectCode == null) {
      fail(result, "reject code is required for order item with rejected quantity");
      return Optional.empty();
    }
    OrderCancelCode orderCancelCode = orderCancelCodeRepository.findByCode(rejectCode).orElse(null);
    if (orderCancelCode == null) {
      fail(result, "reject code " + rejectCode + " not found");
      return Optional.empty();
    }
    return Optional.of(orderCancelCode);
  }
}
