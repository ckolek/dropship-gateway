package me.kolek.ecommerce.dsgw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import me.kolek.ecommerce.dsgw.model.Supplier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogItemRepository extends ExtendedJpaRepository<CatalogItem, Long> {

  @Query("select ci from CatalogItem ci where ci.catalog.supplier = :supplier and ("
      + "ci.sku = :#{#example.sku} or "
      + "ci.gtin = :#{#example.gtin} or "
      + "ci.upc = :#{#example.upc} or "
      + "ci.ean = :#{#example.ean} or "
      + "ci.isbn = :#{#example.isbn}"
      + ")")
  List<CatalogItem> findBySupplierAndExample(Supplier supplier, CatalogItem example);
}
