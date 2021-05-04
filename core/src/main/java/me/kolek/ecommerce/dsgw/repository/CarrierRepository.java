package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.Carrier;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierRepository extends ExtendedJpaRepository<Carrier, Long> {
}
