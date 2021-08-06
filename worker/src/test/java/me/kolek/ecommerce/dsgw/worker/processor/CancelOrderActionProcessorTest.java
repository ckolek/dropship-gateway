package me.kolek.ecommerce.dsgw.worker.processor;

import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.acceptLineExplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.acceptLineImplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.rejectLineExplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.rejectLineImplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.rejectLinePartial;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.execute;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.find;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.resolveStandardOrderCancelCodes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.temporal.ChronoUnit;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AcknowledgeOrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.CancelOrderAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.registry.OrderCancelCodeRegistry;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import me.kolek.ecommerce.dsgw.test.Orders;
import me.kolek.ecommerce.dsgw.test.ReferenceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CancelOrderActionProcessorTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderCancelCodeRegistry orderCancelCodeRegistry;

  @Mock
  private OrderEventEmitter orderEventEmitter;

  @Mock
  private TransactionTemplate transactionTemplate;

  @Mock
  private TransactionStatus transactionStatus;

  private CancelOrderActionProcessor processor;

  @BeforeEach
  public void setUp() {
    processor = new CancelOrderActionProcessor(orderRepository, orderCancelCodeRegistry,
        orderEventEmitter);
    processor.setTransactionTemplate(transactionTemplate);

    execute(transactionTemplate, transactionStatus);
  }

  @Test
  public void testSuccessful() throws Exception {
    var order = Orders.newOrder();

    var cancelCode = ReferenceData.OrderCancelCodes.other();

    var request = CancelOrderRequest.builder()
        .cancelCode(cancelCode.getCode())
        .cancelReason("cancel reason")
        .build();

    var action = CancelOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    find(orderRepository, order, Order::getId);
    resolveStandardOrderCancelCodes(orderCancelCodeRegistry);

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").isNullOrEmpty();
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.SUCCESSFUL);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    assertThat(order.getStatus()).describedAs("order status").isEqualTo(Order.Status.CANCELLED);
    assertThat(order.getCancelCode()).describedAs("order cancel code").isEqualTo(cancelCode);
    assertThat(order.getCancelReason()).describedAs("order cancel reason")
        .isEqualTo(request.getCancelReason());
    assertThat(order.getTimeCancelled()).describedAs("order time cancelled")
        .isCloseToUtcNow(within(1, ChronoUnit.SECONDS));

    assertThat(order.getItems()).describedAs("order items").allSatisfy(orderItem -> {
      assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.CANCELLED);
      assertThat(orderItem.getTimeCancelled()).describedAs("time cancelled")
          .isEqualTo(order.getTimeCancelled());
      assertThat(orderItem.getQuantityCancelled()).describedAs("quantity cancelled")
          .isEqualTo(orderItem.getQuantity());
      assertThat(orderItem.getCancelCode()).describedAs("cancel code")
          .isEqualTo(order.getCancelCode());
      assertThat(orderItem.getCancelReason()).describedAs("cancel reason")
          .isEqualTo(order.getCancelReason());
    });

    verify(transactionStatus, never()).setRollbackOnly();
    verify(orderEventEmitter).emitEvent(order, Type.ORDER_CANCELLED);
  }

  @Test
  public void testFailedOrderNotFound() {
    var request = CancelOrderRequest.builder()
        .build();

    var action = CancelOrderAction.builder()
        .orderId("100001")
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains(action.getOrderId())
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
  public void testFailedOrderAlreadyCancelled() {
    var order = Orders.newOrder();
    order.setStatus(Order.Status.CANCELLED);

    find(orderRepository, order, Order::getId);

    var request = CancelOrderRequest.builder()
        .build();

    var action = CancelOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("has already been cancelled");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedOrderNotCancellable() {
    var order = Orders.newOrder();
    order.setStatus(Order.Status.SHIPPED);

    find(orderRepository, order, Order::getId);

    var request = CancelOrderRequest.builder()
        .build();

    var action = CancelOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("cannot be cancelled");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedCancelCodeMissing() {
    var order = Orders.newOrder();

    find(orderRepository, order, Order::getId);

    var request = CancelOrderRequest.builder()
        .build();

    var action = CancelOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("cancel code is required");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedCancelCodeNotFound() {
    var order = Orders.newOrder();

    find(orderRepository, order, Order::getId);

    var request = CancelOrderRequest.builder()
        .cancelCode(ReferenceData.OrderCancelCodes.other().getCode())
        .build();

    var action = CancelOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("cancel code")
            .contains("not found");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }
}
