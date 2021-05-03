package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import java.util.UUID;
import me.kolek.ecommerce.dsgw.model.Carrier;
import me.kolek.ecommerce.dsgw.model.ServiceLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceLevelRepository extends ExtendedJpaRepository<ServiceLevel, Long> {

  @Query("select sl from ServiceLevel sl where sl.carrier.name = :carrierName and sl.mode = :mode")
  Optional<ServiceLevel> findByCarrierNameAndMode(String carrierName, String mode);
}
