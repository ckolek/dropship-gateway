package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.function.Consumer;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.api.service.OrderActionService;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AcknowledgeOrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AddOrderShipmentAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.CancelOrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.SubmitOrderAction;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class MutationTest {

  private final OrderActionService orderActionService = mock(OrderActionService.class);

  private final Mutation mutation = new Mutation(orderActionService);

  @Test
  public void testSubmitOrder() throws Exception {
    testSubmitOrder(null);
  }

  @Test
  public void testSubmitOrderSync() throws Exception {
    testSubmitOrder(Boolean.FALSE);
  }

  @Test
  public void testSubmitOrderAsync() throws Exception {
    testSubmitOrder(Boolean.TRUE);
  }

  private void testSubmitOrder(Boolean async) throws Exception {
    SubmitOrderRequest request = new SubmitOrderRequest();

    testOrderAction(Mutation::submitOrder, request, async, SubmitOrderAction.class,
        orderAction -> assertThat(orderAction.getOrderId()).isNull());
  }

  @Test
  public void testAcknowledgeOrder() throws Exception {
    testAcknowledgeOrder(null);
  }

  @Test
  public void testAcknowledgeOrderSync() throws Exception {
    testAcknowledgeOrder(Boolean.FALSE);
  }

  @Test
  public void testAcknowledgeOrderAsync() throws Exception {
    testAcknowledgeOrder(Boolean.TRUE);
  }

  private void testAcknowledgeOrder(Boolean async) throws Exception {
    AcknowledgeOrderRequest request = new AcknowledgeOrderRequest();

    String orderId = UUID.randomUUID().toString();

    testOrderAction((mutation, r, a) -> mutation.acknowledgeOrder(orderId, r, a), request, async,
        AcknowledgeOrderAction.class,
        orderAction -> assertThat(orderAction.getOrderId()).isEqualTo(orderId));
  }

  @Test
  public void testCancelOrder() throws Exception {
    testCancelOrder(null);
  }

  @Test
  public void testCancelOrderSync() throws Exception {
    testCancelOrder(Boolean.FALSE);
  }

  @Test
  public void testCancelOrderAsync() throws Exception {
    testCancelOrder(Boolean.TRUE);
  }

  private void testCancelOrder(Boolean async) throws Exception {
    CancelOrderRequest request = new CancelOrderRequest();

    String orderId = UUID.randomUUID().toString();

    testOrderAction((mutation, r, a) -> mutation.cancelOrder(orderId, r, a), request, async,
        CancelOrderAction.class,
        orderAction -> assertThat(orderAction.getOrderId()).isEqualTo(orderId));
  }

  @Test
  public void testAddOrderShipment() throws Exception {
    testAddOrderShipment(null);
  }

  @Test
  public void testAddOrderShipmentSync() throws Exception {
    testAddOrderShipment(Boolean.FALSE);
  }

  @Test
  public void testAddOrderShipmentAsync() throws Exception {
    testAddOrderShipment(Boolean.TRUE);
  }

  private void testAddOrderShipment(Boolean async) throws Exception {
    OrderShipmentRequest request = new OrderShipmentRequest();

    String orderId = UUID.randomUUID().toString();

    testOrderAction((mutation, r, a) -> mutation.addOrderShipment(orderId, r, a), request, async,
        AddOrderShipmentAction.class,
        orderAction -> assertThat(orderAction.getOrderId()).isEqualTo(orderId));
  }

  private <R, A extends OrderAction<R>> void testOrderAction(OrderActionFunction<R> function,
      R request, Boolean async, Class<A> actionClass, Consumer<A> actionVerifier) throws Exception {
    OrderActionResult result = new OrderActionResult();

    when(orderActionService.processOrderAction(any(), anyBoolean())).thenReturn(result);

    OrderActionResult returnedResult = function.apply(mutation, request, async);

    assertThat(returnedResult).isSameAs(result);

    ArgumentCaptor<OrderAction<?>> orderActionCaptor = ArgumentCaptor.forClass(OrderAction.class);

    verify(orderActionService)
        .processOrderAction(orderActionCaptor.capture(), eq(Boolean.TRUE.equals(async)));

    assertThat(orderActionCaptor.getValue()).isInstanceOfSatisfying(actionClass, orderAction -> {
      assertThat(orderAction.getRequest()).isSameAs(request);
      actionVerifier.accept(orderAction);
    });
  }

  private interface OrderActionFunction<R> {

    OrderActionResult apply(Mutation mutation, R request, Boolean async) throws Exception;
  }
}
