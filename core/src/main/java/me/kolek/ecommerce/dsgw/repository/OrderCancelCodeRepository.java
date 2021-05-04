package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import me.kolek.ecommerce.dsgw.model.OrderCancelCode;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCancelCodeRepository extends ExtendedJpaRepository<OrderCancelCode, Long> {

  Optional<OrderCancelCode> findByCode(String code);
}
