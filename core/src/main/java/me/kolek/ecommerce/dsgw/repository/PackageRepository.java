package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.Package;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends ExtendedJpaRepository<Package, Long> {
}
