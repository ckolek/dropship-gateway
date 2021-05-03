package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import java.util.UUID;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends ExtendedJpaRepository<OrderItem, Long> {
}
