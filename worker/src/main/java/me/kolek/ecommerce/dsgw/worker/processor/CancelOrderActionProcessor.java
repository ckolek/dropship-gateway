package me.kolek.ecommerce.dsgw.worker.processor;

import java.time.OffsetDateTime;
import javax.inject.Inject;
import lombok.SneakyThrows;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.OrderActionResultBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.CancelOrderAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.Order.Status;
import me.kolek.ecommerce.dsgw.model.OrderCancelCode;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import me.kolek.ecommerce.dsgw.repository.OrderCancelCodeRepository;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class CancelOrderActionProcessor extends BaseOrderActionProcessor<CancelOrderAction> {

  private final OrderCancelCodeRepository orderCancelCodeRepository;
  private final OrderEventEmitter eventEmitter;

  @Inject
  public CancelOrderActionProcessor(OrderRepository orderRepository,
      OrderCancelCodeRepository orderCancelCodeRepository, OrderEventEmitter eventEmitter) {
    super(orderRepository);
    this.orderCancelCodeRepository = orderCancelCodeRepository;
    this.eventEmitter = eventEmitter;
  }

  @Override
  public String getActionType() {
    return CancelOrderAction.TYPE;
  }

  @Override
  protected void process(CancelOrderAction action, Order order, OrderActionResultBuilder result) {
    CancelOrderRequest request = action.getRequest();

    if (order.getStatus() == Status.CANCELLED) {
      fail(result, "order has already been cancelled");
      return;
    }
    if (order.getStatus() != Status.NEW) {
      fail(result, "order with status " + order.getStatus() + " cannot be cancelled");
      return;
    }

    OrderCancelCode cancelCode = orderCancelCodeRepository.findByCode(request.getCancelCode())
        .orElse(null);
    if (cancelCode == null) {
      fail(result, "cancel code " + request.getCancelCode() + " not found");
      return;
    }

    OffsetDateTime timeCancelled = OffsetDateTime.now();

    cancelOrder(order, cancelCode, request.getCancelReason(), timeCancelled);
    cancelItems(order, cancelCode, request.getCancelReason(), timeCancelled);
  }

  @Override
  @SneakyThrows
  protected Order processSuccessful(CancelOrderAction action, Order order) {
    eventEmitter.emitEvent(order, Type.ORDER_CANCELLED);

    return order;
  }

  private void cancelOrder(Order order, OrderCancelCode cancelCode, String cancelReason,
      OffsetDateTime timeCancelled) {
    order.setStatus(Status.CANCELLED);
    order.setCancelCode(cancelCode);
    order.setCancelReason(cancelReason);
    order.setTimeCancelled(timeCancelled);
  }

  private void cancelItems(Order order, OrderCancelCode cancelCode, String cancelReason,
      OffsetDateTime timeCancelled) {
    for (OrderItem item : order.getItems()) {
      cancelItem(item, cancelCode, cancelReason, timeCancelled);
    }
  }

  private void cancelItem(OrderItem item, OrderCancelCode cancelCode, String cancelReason,
      OffsetDateTime timeCancelled) {
    item.setStatus(Status.CANCELLED);
    item.setQuantityCancelled(item.getQuantity());
    item.setCancelCode(cancelCode);
    item.setCancelReason(cancelReason);
    item.setTimeCancelled(timeCancelled);
  }
}
