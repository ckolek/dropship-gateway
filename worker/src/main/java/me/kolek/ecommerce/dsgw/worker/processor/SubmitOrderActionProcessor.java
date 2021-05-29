package me.kolek.ecommerce.dsgw.worker.processor;

import javax.inject.Inject;
import lombok.SneakyThrows;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.OrderActionResultBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.SubmitOrderAction;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import me.kolek.ecommerce.dsgw.model.mapper.OrderItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.OrderMapper;
import me.kolek.ecommerce.dsgw.registry.CarrierRegistry;
import me.kolek.ecommerce.dsgw.registry.WarehouseRegistry;
import me.kolek.ecommerce.dsgw.repository.CatalogItemRepository;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class SubmitOrderActionProcessor extends BaseOrderActionProcessor<SubmitOrderAction> {

  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;
  private final WarehouseRegistry warehouseRegistry;
  private final CatalogItemRepository catalogItemRepository;
  private final CarrierRegistry carrierRegistry;
  private final OrderEventEmitter eventEmitter;

  @Inject
  public SubmitOrderActionProcessor(OrderRepository orderRepository, OrderMapper orderMapper,
      OrderItemMapper orderItemMapper, WarehouseRegistry warehouseRegistry, CatalogItemRepository catalogItemRepository,
      CarrierRegistry carrierRegistry, OrderEventEmitter eventEmitter) {
    super(orderRepository);
    this.orderMapper = orderMapper;
    this.orderItemMapper = orderItemMapper;
    this.warehouseRegistry = warehouseRegistry;
    this.catalogItemRepository = catalogItemRepository;
    this.carrierRegistry = carrierRegistry;
    this.eventEmitter = eventEmitter;
  }

  @Override
  public String getActionType() {
    return SubmitOrderAction.TYPE;
  }

  @Override
  protected void process(SubmitOrderAction action, Order order, OrderActionResultBuilder result) {
    SubmitOrderRequest request = action.getRequest();

    order = orderMapper.submitOrderRequestToOrder(request, order);

    processWarehouse(request, order, result);
    processItems(request, order, result);
    processServiceLevel(request, order, result);
  }

  @Override
  @SneakyThrows
  protected Order processSuccessful(SubmitOrderAction action, Order order) {
    order = orderRepository.save(order);

    eventEmitter.emitEvent(order, Type.ORDER_CREATED);

    return order;
  }

  @Override
  protected Order findOrder(SubmitOrderAction action, OrderActionResultBuilder result) {
    String orderNumber = action.getRequest().getOrderNumber();
    if (orderRepository.existsByExternalId(orderNumber)) {
      fail(result, "order " + orderNumber + " already exists");
      return null;
    }
    return new Order();
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

    OrderItem orderItem = orderItemMapper.submitOrderItemToOrderItem(item);
    orderItem.setLineNumber(lineNumber);
    orderItem.setCatalogItem(candidates.get(0));

    order.addItem(orderItem);
  }

  private void processServiceLevel(SubmitOrderRequest request, Order order,
      OrderActionResult.OrderActionResultBuilder result) {
    if (request.getCarrierName() == null ^ request.getCarrierMode() == null) {
      fail(result, "both carrier name and mode are required to identify service level");
      return;
    }
    if (request.getCarrierName() != null) {
      carrierRegistry
          .findServiceLevelByCarrierNameAndMode(request.getCarrierName(), request.getCarrierMode())
          .ifPresentOrElse(order::setServiceLevel, () -> fail(result,
              "carrier/service level \"" + request.getCarrierName() + "/" + request.getCarrierMode()
                  + "\" not found"));
    } else if (request.getCarrierServiceLevelCode() != null) {
      carrierRegistry.findServiceLevelByCode(request.getCarrierServiceLevelCode())
          .ifPresentOrElse(order::setServiceLevel, () -> fail(result,
              "carrier service level \"" + request.getCarrierServiceLevelCode() + "\" not found"));
    }
  }
}
