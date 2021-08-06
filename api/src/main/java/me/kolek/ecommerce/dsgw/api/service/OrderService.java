package me.kolek.ecommerce.dsgw.api.service;

import com.google.common.primitives.Longs;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.graphql.service.DataFetchingEnvironmentMappingFieldSelection;
import me.kolek.ecommerce.dsgw.api.model.OrderConnection;
import me.kolek.ecommerce.dsgw.api.model.OrderCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderEdge;
import me.kolek.ecommerce.dsgw.api.util.PageUtil;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.mapper.OrderMapper;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import me.kolek.ecommerce.dsgw.search.OrderSearch;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderSearch orderSearch;

  @Transactional
  public OrderDTO findOrderById(String id, DataFetchingEnvironment environment) {
    return Optional.of(id).map(Longs::tryParse).flatMap(orderRepository::findById)
        .map(order -> mapOrder(order, environment)).orElse(null);
  }

  @Transactional
  public OrderDTO findOrderByOrderNumber(String orderNumber, DataFetchingEnvironment environment) {
    return Optional.of(orderNumber).flatMap(orderRepository::findByExternalId)
        .map(order -> mapOrder(order, environment)).orElse(null);
  }

  public OrderConnection searchOrders(OrderCriteriaDTO criteria, int pageNumber, int pageSize) {
    var results = orderSearch.searchOrders(criteria, PageRequest.of(pageNumber, pageSize));
    return PageUtil.toConnection(results, OrderEdge::new, OrderConnection::new);
  }

  private OrderDTO mapOrder(Order order, DataFetchingEnvironment environment) {
    return orderMapper.orderToDto(order,
        DataFetchingEnvironmentMappingFieldSelection.fromEnvironment(environment));
  }
}
