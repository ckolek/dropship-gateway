package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import me.kolek.ecommerce.dsgw.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ExtendedJpaRepository<Order, Long> {

  boolean existsByExternalId(String externalId);

  Optional<Order> findByExternalId(String externalId);
}
