package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends ExtendedJpaRepository<OrderItem, Long> {
}
