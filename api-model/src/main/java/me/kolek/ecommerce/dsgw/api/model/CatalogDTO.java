package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CatalogDTO {
  private String id;
  private String externalId;
  private SupplierDTO supplier;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<CatalogEntryDTO> items;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
