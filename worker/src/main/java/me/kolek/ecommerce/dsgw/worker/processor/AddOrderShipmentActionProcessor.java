package me.kolek.ecommerce.dsgw.worker.processor;

import javax.inject.Inject;
import lombok.SneakyThrows;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.OrderActionResultBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentRequest;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AddOrderShipmentAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.Order.Status;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import me.kolek.ecommerce.dsgw.model.Package;
import me.kolek.ecommerce.dsgw.model.PackageItem;
import me.kolek.ecommerce.dsgw.model.mapper.PackageItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.PackageMapper;
import me.kolek.ecommerce.dsgw.registry.CarrierRegistry;
import me.kolek.ecommerce.dsgw.registry.WarehouseRegistry;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import me.kolek.ecommerce.dsgw.repository.PackageRepository;
import me.kolek.ecommerce.dsgw.util.OrderUtil;
import org.springframework.stereotype.Component;

@Component
public class AddOrderShipmentActionProcessor extends BaseOrderActionProcessor<AddOrderShipmentAction>  {

  private final PackageMapper packageMapper;
  private final PackageItemMapper packageItemMapper;
  private final PackageRepository packageRepository;
  private final WarehouseRegistry warehouseRegistry;
  private final CarrierRegistry carrierRegistry;
  private final OrderEventEmitter eventEmitter;

  @Inject
  public AddOrderShipmentActionProcessor(OrderRepository orderRepository,
      PackageMapper packageMapper, PackageItemMapper packageItemMapper,
      PackageRepository packageRepository, WarehouseRegistry warehouseRegistry,
      CarrierRegistry carrierRegistry, OrderEventEmitter eventEmitter) {
    super(orderRepository);
    this.packageMapper = packageMapper;
    this.packageItemMapper = packageItemMapper;
    this.packageRepository = packageRepository;
    this.warehouseRegistry = warehouseRegistry;
    this.carrierRegistry = carrierRegistry;
    this.eventEmitter = eventEmitter;
  }

  @Override
  public String getActionType() {
    return AddOrderShipmentAction.TYPE;
  }

  @Override
  protected void process(AddOrderShipmentAction action, Order order,
      OrderActionResultBuilder result) {
    var request = action.getRequest();

    var _package = packageMapper.orderShipmentRequestToPackage(request);
    order.addPackage(_package);

    processWarehouse(request, _package, result);
    processServiceLevel(request, _package, result);
    processItems(request, _package, result);
    processStatus(order, result);
    
    packageRepository.save(_package);
  }

  @Override
  @SneakyThrows
  protected void processSuccessful(AddOrderShipmentAction action, Order order) {
    switch (order.getStatus()) {
      case SHIPPED_PARTIAL -> eventEmitter.emitEvent(order, Type.ORDER_SHIPPED_PARTIAL);
      case SHIPPED -> eventEmitter.emitEvent(order, Type.ORDER_SHIPPED);
    }
  }

  private void processWarehouse(OrderShipmentRequest request, Package _package,
      OrderActionResult.OrderActionResultBuilder result) {
    String warehouseCode = request.getWarehouseCode();

    warehouseRegistry.findWarehouseBySupplierCode(warehouseCode)
        .ifPresentOrElse(_package::setWarehouse,
            () -> fail(result, "warehouse with supplier code \"" + warehouseCode + "\" not found"));
  }

  private void processServiceLevel(OrderShipmentRequest request, Package _package,
      OrderActionResult.OrderActionResultBuilder result) {
    if (request.getCarrierName() == null ^ request.getCarrierMode() == null) {
      fail(result, "both carrier name and mode are required to identify service level");
      return;
    }
    if (request.getCarrierName() != null) {
      carrierRegistry
          .findServiceLevelByCarrierNameAndMode(request.getCarrierName(), request.getCarrierMode())
          .ifPresentOrElse(_package::setServiceLevel, () -> fail(result,
              "carrier/service level \"" + request.getCarrierName() + "/" + request.getCarrierMode()
                  + "\" not found"));
    } else if (request.getCarrierServiceLevelCode() != null) {
      carrierRegistry.findServiceLevelByCode(request.getCarrierServiceLevelCode())
          .ifPresentOrElse(_package::setServiceLevel, () -> fail(result,
              "carrier service level \"" + request.getCarrierServiceLevelCode() + "\" not found"));
    }
  }

  private void processItems(OrderShipmentRequest request, Package _package,
      OrderActionResult.OrderActionResultBuilder result) {
    for (OrderShipmentItem orderShipmentItem : request.getItems()) {
      processItem(orderShipmentItem, _package, result);
    }
  }

  private void processItem(OrderShipmentItem item, Package _package,
      OrderActionResultBuilder result) {
    PackageItem packageItem = packageItemMapper.orderShipmentItemToPackageItem(item);
    _package.addItem(packageItem);

    OrderUtil.getOrderItemByLineNumber(_package.getOrder(), item.getOrderLineNumber())
        .ifPresentOrElse(orderItem -> orderItem.addPackageItem(packageItem), () -> fail(result,
            "order item with line number " + item.getOrderLineNumber() + " not found"));
  }

  private void processStatus(Order order,
      OrderActionResult.OrderActionResultBuilder result) {
    boolean allComplete = true;
    for (OrderItem orderItem : order.getItems()) {
      allComplete &= processItemStatus(orderItem, result);
    }

    order.setStatus(allComplete ? Status.SHIPPED : Status.SHIPPED_PARTIAL);
  }

  private boolean processItemStatus(OrderItem orderItem,
      OrderActionResult.OrderActionResultBuilder result) {
    int quantityShipped = OrderUtil.getQuantityShipped(orderItem);
    if (quantityShipped < orderItem.getQuantityAccepted()) {
      orderItem.setStatus(Status.SHIPPED_PARTIAL);
      return false;
    } else if (quantityShipped == orderItem.getQuantityAccepted()) {
      orderItem.setStatus(Status.SHIPPED);
      return true;
    } else {
      fail(result, "quantity shipped for order item with line number " + orderItem.getLineNumber()
          + " is greater than quantity accepted");
      return false;
    }
  }
}
