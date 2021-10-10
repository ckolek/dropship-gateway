package me.kolek.ecommerce.dsgw.repository;

import java.util.Optional;
import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCredentialsRepository extends ExtendedJpaRepository<ClientCredentials, Long> {

  Optional<ClientCredentials> findByClientId(String clientId);
}
