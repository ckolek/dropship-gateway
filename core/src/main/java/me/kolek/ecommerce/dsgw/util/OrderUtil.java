package me.kolek.ecommerce.dsgw.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import me.kolek.ecommerce.dsgw.model.PackageItem;

public class OrderUtil {

  public static Map<Integer, OrderItem> mapOrderItemsByLineNumber(Order order) {
    return mapOrderItemsByLineNumber(order, Order::getItems, OrderItem::getLineNumber);
  }

  public static Map<Integer, OrderItemDTO> mapOrderItemsByLineNumber(OrderDTO order) {
    return mapOrderItemsByLineNumber(order, OrderDTO::getItems, OrderItemDTO::getLineNumber);
  }

  private static <O, I> Map<Integer, I> mapOrderItemsByLineNumber(O order,
      Function<O, List<I>> getOrderItems, Function<I, Integer> getItemLineNumber) {
    return getOrderItems.apply(order).stream()
        .collect(Collectors.toMap(getItemLineNumber, Function.identity()));
  }

  public static Optional<OrderItem> getOrderItemByLineNumber(Order order, int lineNumber) {
    return getOrderItemByLineNumber(order, Order::getItems, lineNumber);
  }

  public static Optional<OrderItemDTO> getOrderItemByLineNumber(OrderDTO order, int lineNumber) {
    return getOrderItemByLineNumber(order, OrderDTO::getItems, lineNumber);
  }

  private static <O, I> Optional<I> getOrderItemByLineNumber(O order,
      Function<O, List<I>> getOrderItems, int lineNumber) {
    List<I> items = getOrderItems.apply(order);
    if (lineNumber <= 0 || lineNumber > items.size()) {
      return Optional.empty();
    }
    return Optional.of(items.get(lineNumber - 1));
  }

  public static int getQuantityShipped(OrderItem orderItem) {
    return orderItem.getPackageItems().stream().mapToInt(PackageItem::getQuantity).sum();
  }
}
