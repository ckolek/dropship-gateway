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

  protected TransactionTemplate transactionTemplate;

  @Inject
  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    this.transactionTemplate = transactionTemplate;
  }

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
        case SUCCESSFUL -> {
          order = orderRepository.saveAndFlush(order);
          processSuccessful(action, order);
        }
        case FAILED -> {
          status.setRollbackOnly();
          processFailed(action, order);
        }
      }

      Optional.ofNullable(order).map(Order::getId).map(Object::toString)
          .ifPresent(result::setOrderId);

      return result;
    });
  }

  protected abstract void process(A action, Order order,
      OrderActionResult.OrderActionResultBuilder result);

  protected void processSuccessful(A action, Order order) {
  }

  protected void processFailed(A action, Order order) {
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
