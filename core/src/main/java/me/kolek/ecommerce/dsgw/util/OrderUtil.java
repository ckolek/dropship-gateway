package me.kolek.ecommerce.dsgw.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.OrderItem;

public class OrderUtil {
  public static Map<Integer, OrderItem> mapOrderItemsByLineNumber(Order order) {
    return order.getItems().stream()
        .collect(Collectors.toMap(OrderItem::getLineNumber, Function.identity()));
  }
}
