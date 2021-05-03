package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CatalogDTO {
  private String id;
  private String externalId;
  private SupplierDTO supplier;
  private List<CatalogItemDTO> items;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
