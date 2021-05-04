package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends ExtendedJpaRepository<Supplier, Long> {
}
