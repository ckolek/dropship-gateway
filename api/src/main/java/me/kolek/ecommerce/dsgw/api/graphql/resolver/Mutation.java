package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLMutationResolver;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.api.service.OrderActionService;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AcknowledgeOrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.CancelOrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.SubmitOrderAction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class Mutation implements GraphQLMutationResolver {

  private final OrderActionService orderActionService;

  public OrderActionResult submitOrder(SubmitOrderRequest request, Boolean async) throws Exception {
    return processOrderAction(SubmitOrderAction.builder().request(request).build(), async);
  }

  public OrderActionResult acknowledgeOrder(String orderId, AcknowledgeOrderRequest request, Boolean async)
      throws Exception {
    return processOrderAction(
        AcknowledgeOrderAction.builder().orderId(orderId).request(request).build(), async);
  }

  public OrderActionResult cancelOrder(String orderId, CancelOrderRequest request, Boolean async)
      throws Exception {
    return processOrderAction(CancelOrderAction.builder().orderId(orderId).request(request).build(),
        async);
  }

  private OrderActionResult processOrderAction(OrderAction<?> orderAction, Boolean async)
      throws Exception {
    return orderActionService.processOrderAction(orderAction, Boolean.TRUE.equals(async));
  }
}
