package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventoryDTO {
  private String id;
  private CatalogEntryDTO catalogEntry;
  private WarehouseDTO warehouse;
  private Integer quantityAvailable;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
