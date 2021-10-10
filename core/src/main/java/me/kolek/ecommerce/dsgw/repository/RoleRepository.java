package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends ExtendedJpaRepository<Role, Long> {

}
