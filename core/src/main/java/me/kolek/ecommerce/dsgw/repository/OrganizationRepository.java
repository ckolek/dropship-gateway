package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import me.kolek.ecommerce.dsgw.model.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends ExtendedJpaRepository<Organization, Long> {

}
