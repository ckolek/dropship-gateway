package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.Catalog;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends ExtendedJpaRepository<Catalog, Long> {
}
