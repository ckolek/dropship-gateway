package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import graphql.schema.DataFetchingEnvironment;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.service.OrderService;
import org.junit.jupiter.api.Test;

public class QueryTest {

  private final OrderService orderService = mock(OrderService.class);
  private final DataFetchingEnvironment environment = mock(DataFetchingEnvironment.class);

  private final Query query = new Query(orderService);

  @Test
  public void testOrderMissingParameters() {
    assertThatThrownBy(() -> query.order(null, null, environment))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testOrderByIdNotExists() {
    var order = query.order("123", null, environment);
    assertThat(order).isNull();
  }

  @Test
  public void testOrderById() {
    OrderDTO order = new OrderDTO();
    when(orderService.findOrderById("123", environment)).thenReturn(order);

    var returnedOrder = query.order("123", null, environment);
    assertThat(returnedOrder).isSameAs(order);
  }

  @Test
  public void testOrderByOrderNumberNotExists() {
    var order = query.order(null, "123", environment);
    assertThat(order).isNull();
  }

  @Test
  public void testOrderByOrderNumber() {
    OrderDTO order = new OrderDTO();
    when(orderService.findOrderByOrderNumber("123", environment)).thenReturn(order);

    var returnedOrder = query.order(null, "123", environment);
    assertThat(returnedOrder).isSameAs(order);
  }
}
