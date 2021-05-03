package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import me.kolek.ecommerce.dsgw.model.Warehouse;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends ExtendedJpaRepository<Warehouse, Long> {

  Optional<Warehouse> findWarehouseByCode(String code);
}
