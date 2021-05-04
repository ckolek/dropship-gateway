package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public abstract class CatalogEntryDTO {
  private String id;
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
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
