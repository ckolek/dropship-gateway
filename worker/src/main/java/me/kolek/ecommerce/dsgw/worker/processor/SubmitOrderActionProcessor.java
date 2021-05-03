package me.kolek.ecommerce.dsgw.worker.processor;

import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Reason;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.internal.model.order.action.submit.SubmitOrderAction;
import me.kolek.ecommerce.dsgw.model.Address;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import me.kolek.ecommerce.dsgw.model.Contact;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.Order.Status;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import me.kolek.ecommerce.dsgw.registry.CarrierRegistry;
import me.kolek.ecommerce.dsgw.registry.WarehouseRegistry;
import me.kolek.ecommerce.dsgw.repository.CatalogItemRepository;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class SubmitOrderActionProcessor implements OrderActionProcessor<SubmitOrderAction> {

  private final OrderRepository orderRepository;
  private final WarehouseRegistry warehouseRegistry;
  private final CatalogItemRepository catalogItemRepository;
  private final CarrierRegistry carrierRegistry;

  @Override
  public String getActionType() {
    return SubmitOrderAction.TYPE;
  }

  @Override
  public OrderActionResult process(SubmitOrderAction action) {
    var request = action.getRequest();

    if (orderRepository.existsByExternalId(request.getOrderNumber())) {
      var result = OrderActionResult.builder();
      fail(result, "order " + request.getOrderNumber() + " already exists");
      return result.build();
    }

    var resultBuilder = OrderActionResult.builder()
        .status(OrderActionResult.Status.SUCCESSFUL);

    Order order = process(request, resultBuilder);

    var result = resultBuilder.build();

    if (result.getStatus() == OrderActionResult.Status.SUCCESSFUL) {
      order = orderRepository.save(order);
      result.setOrderId(order.getId().toString());
    }

    return result;
  }

  private Order process(SubmitOrderRequest request,
      OrderActionResult.OrderActionResultBuilder result) {
    var order = Order.builder()
        .externalId(request.getOrderNumber())
        .customerOrderNumber(request.getCustomerOrderNumber())
        .contact(Contact.builder()
            .name(request.getRecipient().getName())
            .email(request.getRecipient().getEmail())
            .phone(request.getRecipient().getPhone())
            .build())
        .address(Address.builder()
            .line1(request.getRecipient().getLine1())
            .line2(request.getRecipient().getLine2())
            .line3(request.getRecipient().getLine3())
            .city(request.getRecipient().getCity())
            .state(request.getRecipient().getState())
            .province(request.getRecipient().getProvince())
            .postalCode(request.getRecipient().getPostalCode())
            .country(request.getRecipient().getCountry())
            .build())
        .status(Status.NEW)
        .timeOrdered(request.getTimeOrdered())
        .timeReleased(request.getTimeReleased())
        .build();

    processWarehouse(request, order, result);
    processItems(request, order, result);
    processServiceLevel(request, order, result);

    return order;
  }

  private void processWarehouse(SubmitOrderRequest request, Order order,
      OrderActionResult.OrderActionResultBuilder result) {
    String warehouseCode = request.getWarehouseCode();

    warehouseRegistry.findWarehouseByCode(request.getWarehouseCode())
        .ifPresentOrElse(order::setWarehouse,
            () -> fail(result, "warehouse with code \"" + warehouseCode + "\" not found"));
  }

  private void processItems(SubmitOrderRequest request, Order order,
      OrderActionResult.OrderActionResultBuilder result) {
    if (order.getWarehouse() == null) {
      return;
    }

    for (var iter = request.getItems().listIterator(); iter.hasNext(); ) {
      processItem(iter.next(), iter.nextIndex(), order, result);
    }
  }

  private void processItem(SubmitOrderItem item, int lineNumber, Order order,
      OrderActionResult.OrderActionResultBuilder result) {
    CatalogItem catalogItem = CatalogItem.builder().sku(item.getSku()).gtin(item.getGtin())
        .upc(item.getUpc()).ean(item.getEan()).isbn(item.getIsbn()).build();

    var candidates = catalogItemRepository
        .findBySupplierAndExample(order.getWarehouse().getSupplier(), catalogItem);

    if (candidates.isEmpty()) {
      fail(result, "catalog item not found for line number " + lineNumber);
      return;
    }
    if (candidates.size() > 1) {
      fail(result, "multiple catalog items found for line number " + lineNumber);
      return;
    }

    order.addItem(OrderItem.builder()
        .lineNumber(lineNumber)
        .catalogItem(candidates.get(0))
        .quantity(item.getQuantity())
        .customization(item.getCustomization())
        .expectedShipDate(item.getExpectedShipDate())
        .expectedDeliveryDate(item.getExpectedDeliveryDate())
        .build());
  }

  private void processServiceLevel(SubmitOrderRequest request, Order order,
      OrderActionResult.OrderActionResultBuilder result) {
    if (request.getCarrierName() == null ^ request.getCarrierMode() == null) {
      fail(result, "both carrier name and mode are required to identify service level");
      return;
    }
    if (request.getCarrierName() == null) {
      return;
    }
    carrierRegistry
        .findServiceLevelByCarrierNameAndMode(request.getCarrierName(), request.getCarrierMode())
        .ifPresentOrElse(order::setServiceLevel,
            () -> fail(result, "carrier service level not found"));
  }

  private void fail(OrderActionResult.OrderActionResultBuilder result, String reasonDescription) {
    result.status(OrderActionResult.Status.FAILED);
    result.reason(Reason.builder()
        .description(reasonDescription)
        .build());
  }
}