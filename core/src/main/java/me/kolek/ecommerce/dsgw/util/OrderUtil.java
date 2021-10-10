package me.kolek.ecommerce.dsgw.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.api.model.PackageItemDTO;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import me.kolek.ecommerce.dsgw.model.PackageItem;

public class OrderUtil {

  public static Map<Integer, OrderItem> collectOrderItemsByLineNumber(Order order) {
    return collectOrderItemsByLineNumber(order, Order::getItems, OrderItem::getLineNumber);
  }

  public static Map<Integer, OrderItemDTO> collectOrderItemsByLineNumber(OrderDTO order) {
    return collectOrderItemsByLineNumber(order, OrderDTO::getItems, OrderItemDTO::getLineNumber);
  }

  private static <O, I> Map<Integer, I> collectOrderItemsByLineNumber(O order,
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
    return getQuantityShipped(orderItem, OrderItem::getPackageItems,
      PackageItem::getQuantity);
  }

  public static int getQuantityShipped(OrderItemDTO orderItem) {
    return getQuantityShipped(orderItem, OrderItemDTO::getPackageItems,
        PackageItemDTO::getQuantity);
  }

  private static <OI, PI> int getQuantityShipped(OI orderItem,
      Function<OI, Collection<PI>> getPackageItems, ToIntFunction<PI> getPackageItemQuantity) {
    Collection<PI> packageItems = getPackageItems.apply(orderItem);
    if (packageItems == null) {
      return 0;
    }
    return packageItems.stream().mapToInt(getPackageItemQuantity).sum();
  }

  public static int getQuantityAccepted(OrderItem orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityAccepted(), 0);
  }

  public static int getQuantityAccepted(OrderItemDTO orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityAccepted(), 0);
  }

  public static int getQuantityRejected(OrderItem orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityRejected(), 0);
  }

  public static int getQuantityRejected(OrderItemDTO orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityRejected(), 0);
  }

  public static int getQuantityCancelled(OrderItem orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityCancelled(), 0);
  }

  public static int getQuantityCancelled(OrderItemDTO orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityCancelled(), 0);
  }

  public static int getQuantityExpected(OrderItem orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityAccepted(), orderItem.getQuantity()) - (
        getQuantityRejected(orderItem) + getQuantityCancelled(orderItem));
  }

  public static int getQuantityExpected(OrderItemDTO orderItem) {
    return Objects.requireNonNullElse(orderItem.getQuantityAccepted(), orderItem.getQuantity()) - (
        getQuantityRejected(orderItem) + getQuantityCancelled(orderItem));
  }

  public static int getQuantityRemaining(OrderItem orderItem) {
    return getQuantityExpected(orderItem) - getQuantityShipped(orderItem);
  }

  public static int getQuantityRemaining(OrderItemDTO orderItem) {
    return getQuantityExpected(orderItem) - getQuantityShipped(orderItem);
  }

  public static boolean hasQuantityRemaining(OrderItem orderItem) {
    return getQuantityRemaining(orderItem) > 0;
  }

  public static boolean hasQuantityRemaining(OrderItemDTO orderItem) {
    return getQuantityRemaining(orderItem) > 0;
  }
}
