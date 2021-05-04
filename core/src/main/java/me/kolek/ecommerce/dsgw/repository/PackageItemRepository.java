package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.PackageItem;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageItemRepository extends ExtendedJpaRepository<PackageItem, Long> {
}
