package me.kolek.ecommerce.dsgw.api.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@JsonTypeInfo(
    use = Id.NAME,
    property = "type",
    defaultImpl = CatalogEntryDTO.class)
@JsonSubTypes({
    @Type(name = CatalogItemDTO.TYPE, value = CatalogItemDTO.class),
    @Type(name = CatalogItemOptionDTO.TYPE, value = CatalogItemOptionDTO.class)
})
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
