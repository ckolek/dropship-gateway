package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import me.kolek.ecommerce.dsgw.model.ServiceLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceLevelRepository extends ExtendedJpaRepository<ServiceLevel, Long> {

  @Query("select sl from ServiceLevel sl where sl.carrier.name = :carrierName and sl.mode = :mode")
  Optional<ServiceLevel> findByCarrierNameAndMode(String carrierName, String mode);

  Optional<ServiceLevel> findByCode(String code);
}
