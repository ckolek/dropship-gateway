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
public class CatalogEntryDTO {
  private String id;
  private CatalogDTO catalog;
  private String name;
  private String shortDescription;
  private String longDescription;
  private String sku;
  private String mpn;
  private String gtin;
  private String upc;
  private String ean;
  private String isbn;
  private String manufacturer;
  private String brand;
  private CatalogEntryDTO item;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<CatalogEntryDTO> options;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
