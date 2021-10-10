package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import me.kolek.ecommerce.dsgw.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ExtendedJpaRepository<User, Long> {

}
