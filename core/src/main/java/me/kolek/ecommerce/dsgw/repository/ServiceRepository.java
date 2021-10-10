package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import me.kolek.ecommerce.dsgw.model.Service;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends ExtendedJpaRepository<Service, Long> {

}
