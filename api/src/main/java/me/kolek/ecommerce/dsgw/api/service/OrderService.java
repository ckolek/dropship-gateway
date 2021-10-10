package me.kolek.ecommerce.dsgw.api.service;

import static me.kolek.ecommerce.dsgw.auth.ScopeConstants.ORG;
import static me.kolek.ecommerce.dsgw.auth.ScopeConstants.ORG_ID;
import static me.kolek.ecommerce.dsgw.auth.ScopeConstants.ORG_TYPE;
import static me.kolek.ecommerce.dsgw.auth.ScopeConstants.READ;

import com.google.common.primitives.Longs;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.graphql.service.DataFetchingEnvironmentMappingFieldSelection;
import me.kolek.ecommerce.dsgw.api.model.paging.OrderConnection;
import me.kolek.ecommerce.dsgw.api.model.criteria.OrderCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.OrderEdge;
import me.kolek.ecommerce.dsgw.api.util.PageUtil;
import me.kolek.ecommerce.dsgw.auth.aspect.RequireScope;
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
  @RequireScope(action = READ, path = {ORG, ORG_ID, ORG_TYPE, "order"})
  public OrderDTO findOrderById(String id, DataFetchingEnvironment environment) {
    return Optional.of(id).map(Longs::tryParse).flatMap(orderRepository::findById)
        .map(order -> mapOrder(order, environment)).orElse(null);
  }

  @Transactional
  @RequireScope(action = READ, path = {ORG, ORG_ID, ORG_TYPE, "order"})
  public OrderDTO findOrderByOrderNumber(String orderNumber, DataFetchingEnvironment environment) {
    return Optional.of(orderNumber).flatMap(orderRepository::findByExternalId)
        .map(order -> mapOrder(order, environment)).orElse(null);
  }

  @RequireScope(action = READ, path = {ORG, ORG_ID, ORG_TYPE, "order"})
  public OrderConnection searchOrders(OrderCriteriaDTO criteria, int pageNumber, int pageSize) {
    var results = orderSearch.search(criteria, PageRequest.of(pageNumber, pageSize));
    return PageUtil.toConnection(results, OrderEdge::new, OrderConnection::new);
  }

  private OrderDTO mapOrder(Order order, DataFetchingEnvironment environment) {
    return orderMapper.orderToDto(order,
        DataFetchingEnvironmentMappingFieldSelection.fromEnvironment(environment));
  }
}
