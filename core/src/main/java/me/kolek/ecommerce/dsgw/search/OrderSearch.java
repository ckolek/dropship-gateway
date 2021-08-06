package me.kolek.ecommerce.dsgw.search;

import me.kolek.ecommerce.dsgw.api.model.OrderCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderSearch {

  Page<OrderDTO> searchOrders(OrderCriteriaDTO criteria, Pageable pageable);
}
