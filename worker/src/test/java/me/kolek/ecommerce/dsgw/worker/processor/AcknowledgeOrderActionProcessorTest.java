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
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AcknowledgeOrderAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.registry.OrderCancelCodeRegistry;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import me.kolek.ecommerce.dsgw.test.Mappers;
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
public class AcknowledgeOrderActionProcessorTest {
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

  private AcknowledgeOrderActionProcessor processor;

  @BeforeEach
  public void setUp() {
    processor = new AcknowledgeOrderActionProcessor(orderRepository, orderCancelCodeRegistry,
        orderEventEmitter);
    processor.setTransactionTemplate(transactionTemplate);

    execute(transactionTemplate, transactionStatus);
  }

  @Test
  public void testSuccessful() throws Exception {
    var order = Orders.newOrder(6);
    var orderDto = Mappers.order().orderToDto(order);

    var otherRejectCode = ReferenceData.OrderCancelCodes.other();

    var request = AcknowledgeOrderRequest.builder()
        .item(acceptLineImplicit(1))
        .item(acceptLineExplicit(orderDto, 2))
        .item(rejectLineImplicit(3, otherRejectCode.getCode(), "reason 1"))
        .item(rejectLineExplicit(orderDto, 4, otherRejectCode.getCode(), "reason 2"))
        .item(rejectLinePartial(orderDto, 5, otherRejectCode.getCode(), "reason 3"))
        .build();

    var action = AcknowledgeOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    find(orderRepository, order, Order::getId);
    resolveStandardOrderCancelCodes(orderCancelCodeRegistry);

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").isNullOrEmpty();
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.SUCCESSFUL);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    assertThat(order.getStatus()).describedAs("order status").isEqualTo(Order.Status.ACKNOWLEDGED);
    assertThat(order.getTimeAcknowledged()).describedAs("order time acknowledged")
        .isCloseToUtcNow(within(1, ChronoUnit.SECONDS));

    assertThat(order.getItems()).describedAs("order items").element(0).satisfies(orderItem -> {
      assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.ACKNOWLEDGED);
      assertThat(orderItem.getTimeAcknowledged()).describedAs("time acknowledged")
          .isEqualTo(order.getTimeAcknowledged());
      assertThat(orderItem.getQuantityAccepted()).describedAs("quantity accepted")
          .isEqualTo(orderItem.getQuantity());
      assertThat(orderItem.getQuantityRejected()).describedAs("quantity rejected").isZero();
      assertThat(orderItem.getRejectCode()).describedAs("reject code").isNull();
      assertThat(orderItem.getRejectReason()).describedAs("reject reason").isNull();
    });
    assertThat(order.getItems()).describedAs("order items").element(1).satisfies(orderItem -> {
      assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.ACKNOWLEDGED);
      assertThat(orderItem.getTimeAcknowledged()).describedAs("time acknowledged")
          .isEqualTo(order.getTimeAcknowledged());
      assertThat(orderItem.getQuantityAccepted()).describedAs("quantity accepted")
          .isEqualTo(orderItem.getQuantity());
      assertThat(orderItem.getQuantityRejected()).describedAs("quantity rejected").isZero();
      assertThat(orderItem.getRejectCode()).describedAs("reject code").isNull();
      assertThat(orderItem.getRejectReason()).describedAs("reject reason").isNull();
    });
    assertThat(order.getItems()).describedAs("order items").element(2).satisfies(orderItem -> {
      assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.ACKNOWLEDGED);
      assertThat(orderItem.getTimeAcknowledged()).describedAs("time acknowledged")
          .isEqualTo(order.getTimeAcknowledged());
      assertThat(orderItem.getQuantityAccepted()).describedAs("quantity accepted").isZero();
      assertThat(orderItem.getQuantityRejected()).describedAs("quantity rejected")
          .isEqualTo(orderItem.getQuantity());
      assertThat(orderItem.getRejectCode()).describedAs("reject code").isSameAs(otherRejectCode);
      assertThat(orderItem.getRejectReason()).describedAs("reject reason").isEqualTo("reason 1");
    });
    assertThat(order.getItems()).describedAs("order items").element(3).satisfies(orderItem -> {
      assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.ACKNOWLEDGED);
      assertThat(orderItem.getTimeAcknowledged()).describedAs("time acknowledged")
          .isEqualTo(order.getTimeAcknowledged());
      assertThat(orderItem.getQuantityAccepted()).describedAs("quantity accepted").isZero();
      assertThat(orderItem.getQuantityRejected()).describedAs("quantity rejected")
          .isEqualTo(orderItem.getQuantity());
      assertThat(orderItem.getRejectCode()).describedAs("reject code").isSameAs(otherRejectCode);
      assertThat(orderItem.getRejectReason()).describedAs("reject reason").isEqualTo("reason 2");
    });
    assertThat(order.getItems()).describedAs("order items").element(4).satisfies(orderItem -> {
      assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.ACKNOWLEDGED);
      assertThat(orderItem.getTimeAcknowledged()).describedAs("time acknowledged")
          .isEqualTo(order.getTimeAcknowledged());
      assertThat(orderItem.getQuantityAccepted()).describedAs("quantity accepted").isPositive();
      assertThat(orderItem.getQuantityRejected()).describedAs("quantity rejected").isPositive();
      assertThat(orderItem.getQuantityAccepted() + orderItem.getQuantityRejected())
          .describedAs("sum of quantities accepted & rejected").isEqualTo(orderItem.getQuantity());
      assertThat(orderItem.getRejectCode()).describedAs("reject code").isSameAs(otherRejectCode);
      assertThat(orderItem.getRejectReason()).describedAs("reject reason").isEqualTo("reason 3");
    });
    assertThat(order.getItems()).describedAs("order items").element(5).satisfies(orderItem -> {
      assertThat(orderItem.getStatus()).describedAs("status").isEqualTo(Order.Status.NEW);
      assertThat(orderItem.getTimeAcknowledged()).describedAs("time acknowledged").isNull();
      assertThat(orderItem.getQuantityAccepted()).describedAs("quantity accepted").isNull();
      assertThat(orderItem.getQuantityRejected()).describedAs("quantity rejected").isNull();
      assertThat(orderItem.getRejectCode()).describedAs("reject code").isNull();
      assertThat(orderItem.getRejectReason()).describedAs("reject reason").isNull();
    });

    verify(transactionStatus, never()).setRollbackOnly();
    verify(orderEventEmitter).emitEvent(order, Type.ORDER_ACKNOWLEDGED);
  }

  @Test
  public void testFailedOrderNotFound() {
    var request = AcknowledgeOrderRequest.builder()
        .build();

    var action = AcknowledgeOrderAction.builder()
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
  public void testFailedOrderAlreadyAcknowledged() {
    var order = Orders.newOrder();
    order.setStatus(Order.Status.ACKNOWLEDGED);

    find(orderRepository, order, Order::getId);

    var request = AcknowledgeOrderRequest.builder()
        .build();

    var action = AcknowledgeOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("has already been acknowledged");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedOrderNotAcknowledgeable() {
    var order = Orders.newOrder();
    order.setStatus(Order.Status.SHIPPED);

    find(orderRepository, order, Order::getId);

    var request = AcknowledgeOrderRequest.builder()
        .build();

    var action = AcknowledgeOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("cannot be acknowledged");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedLineItemNotFound() {
    var order = Orders.newOrder();

    find(orderRepository, order, Order::getId);

    var request = AcknowledgeOrderRequest.builder()
        .item(AcknowledgeOrderItem.builder()
            .lineNumber(3)
            .rejectCode(ReferenceData.OrderCancelCodes.other().getCode())
            .build())
        .build();

    var action = AcknowledgeOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    resolveStandardOrderCancelCodes(orderCancelCodeRegistry);

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("line number")
            .contains("not found");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedRejectCodeMissing() {
    var order = Orders.newOrder();

    find(orderRepository, order, Order::getId);

    var request = AcknowledgeOrderRequest.builder()
        .item(AcknowledgeOrderItem.builder()
            .lineNumber(1)
            .quantityRejected(1)
            .build())
        .build();

    var action = AcknowledgeOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("reject code is required");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedRejectCodeNotFound() {
    var order = Orders.newOrder();

    find(orderRepository, order, Order::getId);

    var request = AcknowledgeOrderRequest.builder()
        .item(AcknowledgeOrderItem.builder()
            .lineNumber(1)
            .rejectCode(ReferenceData.OrderCancelCodes.other().getCode())
            .build())
        .build();

    var action = AcknowledgeOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("reject code")
            .contains("not found");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }

  @Test
  public void testFailedInvalidQuantities() {
    var order = Orders.newOrder();

    find(orderRepository, order, Order::getId);

    var request = AcknowledgeOrderRequest.builder()
        .item(AcknowledgeOrderItem.builder()
            .lineNumber(1)
            .quantityAccepted(0)
            .quantityRejected(0)
            .rejectCode(ReferenceData.OrderCancelCodes.other().getCode())
            .build())
        .build();

    var action = AcknowledgeOrderAction.builder()
        .orderId(order.getId().toString())
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains("quantity accepted")
            .contains("quantity rejected")
            .contains("must be equal to quantity ordered");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isEqualTo(action.getOrderId());

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }
}
