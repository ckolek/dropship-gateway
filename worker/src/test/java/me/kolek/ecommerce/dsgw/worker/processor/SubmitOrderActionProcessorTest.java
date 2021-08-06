package me.kolek.ecommerce.dsgw.worker.processor;

import static me.kolek.ecommerce.dsgw.test.CommonStubbing.execute;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.resolveStandardServiceLevels;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.save;
import static me.kolek.ecommerce.dsgw.test.ModelAssertions.assertThatAddressIsEquivalent;
import static me.kolek.ecommerce.dsgw.test.ModelAssertions.assertThatContactIsEquivalent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.SubmitOrderAction;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.Supplier;
import me.kolek.ecommerce.dsgw.model.Warehouse;
import me.kolek.ecommerce.dsgw.registry.CarrierRegistry;
import me.kolek.ecommerce.dsgw.registry.WarehouseRegistry;
import me.kolek.ecommerce.dsgw.repository.CatalogItemRepository;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import me.kolek.ecommerce.dsgw.test.Mappers;
import me.kolek.ecommerce.dsgw.test.ReferenceData;
import me.kolek.ecommerce.dsgw.test.SubmitOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SubmitOrderActionProcessorTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private WarehouseRegistry warehouseRegistry;

  @Mock
  private CatalogItemRepository catalogItemRepository;

  @Mock
  private CarrierRegistry carrierRegistry;

  @Mock
  private OrderEventEmitter orderEventEmitter;

  @Mock
  private TransactionStatus transactionStatus;

  @Mock
  private TransactionTemplate transactionTemplate;

  private SubmitOrderActionProcessor processor;

  @BeforeEach
  public void setUp() {
    processor = new SubmitOrderActionProcessor(orderRepository, Mappers.order(),
        Mappers.orderItem(), warehouseRegistry, catalogItemRepository, carrierRegistry,
        orderEventEmitter);
    processor.setTransactionTemplate(transactionTemplate);

    execute(transactionTemplate, transactionStatus);
  }

  @Test
  public void testSuccessful() throws Exception {
    var request = SubmitOrder.validRequest();

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    resolveStandardServiceLevels(carrierRegistry);

    Supplier supplier = new Supplier();

    Warehouse warehouse = new Warehouse();
    warehouse.setSupplier(supplier);
    when(warehouseRegistry.findWarehouseByCode(any())).thenReturn(Optional.of(warehouse));

    when(catalogItemRepository.findBySupplierAndExample(eq(supplier), any()))
        .thenAnswer(invocation -> {
          CatalogItem catalogItem = invocation.getArgument(1);
          return List.of(catalogItem);
        });

    save(orderRepository, System::currentTimeMillis, Order::setId);

    var result = processor.process(action);

    assertThat(result.getReasons()).describedAs("result reasons").isNullOrEmpty();
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.SUCCESSFUL);
    assertThat(result.getOrderId()).describedAs("result order ID").isNotEmpty();

    var orderCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository).save(orderCaptor.capture());

    var order = orderCaptor.getValue();
    assertThat(order.getExternalId()).describedAs("external ID")
        .isEqualTo(request.getOrderNumber());
    assertThat(order.getCustomerOrderNumber()).describedAs("customer order number")
        .isEqualTo(request.getCustomerOrderNumber());
    assertThat(order.getWarehouse()).describedAs("warehouse").isSameAs(warehouse);
    assertThat(order.getContact()).describedAs("contact").satisfies(
        contact -> assertThatContactIsEquivalent(contact, request.getRecipient().getContact()));
    assertThat(order.getAddress()).describedAs("address").satisfies(
        address -> assertThatAddressIsEquivalent(address, request.getRecipient().getAddress()));
    assertThat(order.getServiceLevel()).describedAs("service level")
        .isSameAs(ReferenceData.ServiceLevels.fedexHomeDelivery());
    assertThat(order.getPackages()).describedAs("packages").isNullOrEmpty();
    assertThat(order.getInvoices()).describedAs("invoices").isNullOrEmpty();
    assertThat(order.getStatus()).describedAs("status").isEqualTo(Order.Status.NEW);
    assertThat(order.getCancelCode()).describedAs("cancel code").isNull();
    assertThat(order.getCancelReason()).describedAs("cancel reason").isNull();
    assertThat(order.getTimeOrdered()).describedAs("time ordered")
        .isEqualTo(request.getTimeOrdered());
    assertThat(order.getTimeReleased()).describedAs("time released")
        .isEqualTo(request.getTimeReleased());
    assertThat(order.getTimeAcknowledged()).describedAs("time acknowledged").isNull();
    assertThat(order.getTimeCancelled()).describedAs("time cancelled").isNull();

    for (var iter = order.getItems().listIterator(); iter.hasNext(); ) {
      int index = iter.nextIndex();

      assertThat(iter.next()).describedAs("order item " + index).satisfies(orderItem -> {
        var requestItem = request.getItems().get(index);

        assertThat(orderItem.getLineNumber()).describedAs("line number").isEqualTo(index + 1);
        assertThat(orderItem.getCatalogItem()).describedAs("catalog item").isNotNull();
        assertThat(orderItem.getQuantity()).describedAs("quantity")
            .isEqualTo(requestItem.getQuantity());
        assertThat(orderItem.getCustomization()).describedAs("customization")
            .isEqualTo(requestItem.getCustomization());
        assertThat(orderItem.getExpectedShipDate()).describedAs("expected ship date")
            .isEqualTo(requestItem.getExpectedShipDate());
        assertThat(orderItem.getExpectedDeliveryDate()).describedAs("expected delivery date")
            .isEqualTo(requestItem.getExpectedDeliveryDate());
        assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.NEW);
        assertThat(orderItem.getQuantityAccepted()).describedAs("quantity accepted").isNull();
        assertThat(orderItem.getQuantityRejected()).describedAs("quantity rejected").isNull();
        assertThat(orderItem.getRejectCode()).describedAs("reject code").isNull();
        assertThat(orderItem.getRejectReason()).describedAs("reject reason").isNull();
        assertThat(orderItem.getTimeAcknowledged()).describedAs("time acknowledged").isNull();
        assertThat(orderItem.getQuantityCancelled()).describedAs("quantity cancelled").isNull();
        assertThat(orderItem.getCancelCode()).describedAs("cancel code").isNull();
        assertThat(orderItem.getCancelReason()).describedAs("cancel reason").isNull();
        assertThat(orderItem.getTimeCancelled()).describedAs("time cancelled").isNull();
      });
    }

    verify(transactionStatus, never()).setRollbackOnly();
    verify(orderEventEmitter).emitEvent(order, Type.ORDER_CREATED);
  }

  @Test
  public void testFailedOrderExists() {
    var request = SubmitOrder.validRequest();

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    when(orderRepository.existsByExternalId(request.getOrderNumber())).thenReturn(true);

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains(request.getOrderNumber())
            .contains("exists");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedWarehouseNotFound() {
    var request = SubmitOrder.validRequest();

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    var result = processor.process(action);assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains(request.getWarehouseCode())
            .contains("not found");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedServiceLevelNotFoundByCarrierNameAndMode() {
    var request = SubmitOrder.validRequest();

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    var result = processor.process(action);

    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains(request.getWarehouseCode());
      });
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains(request.getCarrierName())
            .contains(request.getCarrierMode());
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedCarrierNameMissing() {
    var request = SubmitOrder.validRequest();
    request.setCarrierName(null);

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    var result = processor.process(action);

    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("both carrier name and mode are required");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedCarrierModeMissing() {
    var request = SubmitOrder.validRequest();
    request.setCarrierMode(null);

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    var result = processor.process(action);

    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("both carrier name and mode are required");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedServiceLevelNotFoundByCode() {
    var request = SubmitOrder.validRequest();
    request.setCarrierName(null);
    request.setCarrierMode(null);
    request.setCarrierServiceLevelCode(ReferenceData.ServiceLevels.fedexHomeDelivery().getCode());

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    var result = processor.process(action);

    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains(request.getCarrierServiceLevelCode());
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedCatalogValidations() {
    var request = SubmitOrder.validRequest();

    var action = SubmitOrderAction.builder()
        .request(request)
        .build();

    resolveStandardServiceLevels(carrierRegistry);

    Supplier supplier = new Supplier();

    Warehouse warehouse = new Warehouse();
    warehouse.setSupplier(supplier);
    when(warehouseRegistry.findWarehouseByCode(any())).thenReturn(Optional.of(warehouse));

    when(catalogItemRepository.findBySupplierAndExample(eq(supplier), any()))
        .thenAnswer(invocation -> {
          CatalogItem catalogItem = invocation.getArgument(1);
          return List.of();
        })
        .thenAnswer(invocation -> {
          CatalogItem catalogItem = invocation.getArgument(1);
          return List.of(catalogItem, catalogItem);
        });

    var result = processor.process(action);

    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("catalog item not found");
      });
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("multiple catalog items found");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }
}
