package me.kolek.ecommerce.dsgw.worker.processor;

import com.google.common.primitives.Longs;
import java.util.Optional;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Reason;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public abstract class BaseOrderActionProcessor<A extends OrderAction<?>> implements
    OrderActionProcessor<A> {

  protected final OrderRepository orderRepository;

  @Inject
  protected TransactionTemplate transactionTemplate;

  @Override
  public OrderActionResult process(A action) {
    return transactionTemplate.execute(status -> {
      var resultBuilder = OrderActionResult.builder()
          .status(Status.SUCCESSFUL);

      Order order = findOrder(action, resultBuilder);
      if (order != null) {
        process(action, order, resultBuilder);
      }

      var result = resultBuilder.build();
      switch (result.getStatus()) {
        case SUCCESSFUL:
          order = processSuccessful(action, order);
          break;
        case FAILED:
          order = processFailed(action, order);
          status.setRollbackOnly();
          break;
      }

      Optional.ofNullable(order).map(Order::getId).map(Object::toString)
          .ifPresent(result::setOrderId);

      return result;
    });
  }

  protected abstract void process(A action, Order order,
      OrderActionResult.OrderActionResultBuilder result);

  protected Order processSuccessful(A action, Order order) {
    return order;
  }

  protected Order processFailed(A action, Order order) {
    return order;
  }

  protected Order findOrder(A action, OrderActionResult.OrderActionResultBuilder result) {
    if (action.getOrderId() != null) {
      Order order = Optional.of(action.getOrderId()).map(Longs::tryParse)
          .flatMap(orderRepository::findById).orElse(null);
      if (order == null) {
        fail(result, "order not found with ID " + action.getOrderId());
      }
      return order;
    } else {
      fail(result, "order ID is required");
      return null;
    }
  }

  protected static void fail(OrderActionResult.OrderActionResultBuilder result,
      String reasonDescription) {
    result.status(OrderActionResult.Status.FAILED);
    result.reason(Reason.builder().description(reasonDescription).build());
  }
}
