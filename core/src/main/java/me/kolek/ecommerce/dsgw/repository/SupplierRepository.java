package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import java.util.UUID;
import me.kolek.ecommerce.dsgw.model.Supplier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends ExtendedJpaRepository<Supplier, Long> {
}
