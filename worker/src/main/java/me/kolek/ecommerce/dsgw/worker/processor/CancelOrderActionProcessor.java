package me.kolek.ecommerce.dsgw.worker.processor;

import java.time.OffsetDateTime;
import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.OrderActionResultBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;
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

  @Inject
  public CancelOrderActionProcessor(OrderRepository orderRepository,
      OrderCancelCodeRepository orderCancelCodeRepository) {
    super(orderRepository);
    this.orderCancelCodeRepository = orderCancelCodeRepository;
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

    cancelOrder(order, cancelCode, request.getCancelReason(), OffsetDateTime.now());
  }

  private void cancelOrder(Order order, OrderCancelCode cancelCode, String cancelReason,
      OffsetDateTime now) {
    order.setStatus(Status.CANCELLED);
    order.setCancelCode(cancelCode);
    order.setCancelReason(cancelReason);
    order.setTimeCancelled(now);

    cancelItems(order.getItems(), cancelCode, cancelReason, now);
  }

  private void cancelItems(List<OrderItem> items, OrderCancelCode cancelCode, String cancelReason,
      OffsetDateTime now) {
    for (OrderItem item : items) {
      cancelItem(item, cancelCode, cancelReason, now);
    }
  }

  private void cancelItem(OrderItem item, OrderCancelCode cancelCode, String cancelReason,
      OffsetDateTime now) {
    item.setStatus(Status.CANCELLED);
    item.setQuantityCancelled(item.getQuantity());
    item.setCancelCode(cancelCode);
    item.setCancelReason(cancelReason);
    item.setTimeCancelled(now);
  }
}
