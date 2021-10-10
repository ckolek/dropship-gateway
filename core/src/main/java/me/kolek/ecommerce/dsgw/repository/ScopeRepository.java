package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.Scope;
import org.springframework.stereotype.Repository;

@Repository
public interface ScopeRepository extends ExtendedJpaRepository<Scope, Long> {

}
