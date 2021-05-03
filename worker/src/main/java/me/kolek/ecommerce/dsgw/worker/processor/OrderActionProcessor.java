package me.kolek.ecommerce.dsgw.worker.processor;

import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;
import me.kolek.ecommerce.dsgw.model.Order;

public interface OrderActionProcessor<A extends OrderAction<?>> {
  String getActionType();

  OrderActionResult process(A action) throws Exception;
}
