package me.kolek.ecommerce.dsgw.repository;

import me.kolek.ecommerce.dsgw.model.ItemInventory;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInventoryRepository extends ExtendedJpaRepository<ItemInventory, Long> {
}
