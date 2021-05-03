package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import java.util.UUID;
import me.kolek.ecommerce.dsgw.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ExtendedJpaRepository<Order, Long> {

  boolean existsByExternalId(String externalId);

  Optional<Order> findByExternalId(String externalId);
}
