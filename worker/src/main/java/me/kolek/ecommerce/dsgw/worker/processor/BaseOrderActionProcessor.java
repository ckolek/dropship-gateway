package me.kolek.ecommerce.dsgw.worker.processor;

import com.google.common.primitives.Longs;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Reason;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;

@RequiredArgsConstructor
public abstract class BaseOrderActionProcessor<A extends OrderAction<?>> implements
    OrderActionProcessor<A> {

  protected final OrderRepository orderRepository;

  @Override
  @Transactional
  public OrderActionResult process(A action) throws Exception {
    var resultBuilder = OrderActionResult.builder()
        .status(Status.SUCCESSFUL);

    Order order = findOrder(action, resultBuilder);
    if (order != null) {
      process(action, order, resultBuilder);
    }

    var result = resultBuilder.build();
    if (result.getStatus() == Status.SUCCESSFUL) {
      order = processSuccessful(action, order);
      result.setOrderId(order.getId().toString());
    }

    return result;
  }

  protected abstract void process(A action, Order order,
      OrderActionResult.OrderActionResultBuilder result);

  protected Order processSuccessful(A action, Order order) {
    return order;
  }

  protected Order findOrder(A action, OrderActionResult.OrderActionResultBuilder result) {
    Order order;
    if (action.getOrderId() != null) {
      order = Optional.of(action.getOrderId()).map(Longs::tryParse)
          .flatMap(orderRepository::findById).orElse(null);
      if (order == null) {
        fail(result, "order not found with ID " + action.getOrderId());
      }
    } else {
      order = orderRepository.findByExternalId(action.getOrderNumber()).orElse(null);
      if (order == null) {
        fail(result, "order not found with order number " + action.getOrderNumber());
      }
    }
    return order;
  }

  protected static void fail(OrderActionResult.OrderActionResultBuilder result,
      String reasonDescription) {
    result.status(OrderActionResult.Status.FAILED);
    result.reason(Reason.builder().description(reasonDescription).build());
  }
}
