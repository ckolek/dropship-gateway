package me.kolek.ecommerce.dsgw.api.service;

import com.google.common.primitives.Longs;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.graphql.service.DataFetchingEnvironmentMappingFieldSelection;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.mapper.OrderMapper;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

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

  private OrderDTO mapOrder(Order order, DataFetchingEnvironment environment) {
    return orderMapper.orderToDto(order,
        DataFetchingEnvironmentMappingFieldSelection.fromEnvironment(environment));
  }
}
