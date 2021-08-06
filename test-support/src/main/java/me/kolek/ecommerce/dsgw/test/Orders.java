package me.kolek.ecommerce.dsgw.test;

import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.model.Order;

public class Orders {
  public static Order newOrder(int itemCount) {
    return newOrder(SubmitOrder.validRequest(itemCount));
  }

  public static Order newOrder() {
    return newOrder(SubmitOrder.validRequest());
  }

  private static Order newOrder(SubmitOrderRequest submitOrderRequest) {
    var order = Mappers.order().submitOrderRequestToOrder(submitOrderRequest, new Order());
    order.setId(System.currentTimeMillis());

    for (var iter = submitOrderRequest.getItems().listIterator(); iter.hasNext(); ) {
      var orderItem = Mappers.orderItem().submitOrderItemToOrderItem(iter.next());
      orderItem.setLineNumber(iter.nextIndex());
      order.addItem(orderItem);
    }

    return order;
  }
}
