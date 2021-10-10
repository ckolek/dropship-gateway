package me.kolek.ecommerce.dsgw.api.model.criteria;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CatalogEntryCriteriaDTO {
  private String id;
  private List<CatalogCriteriaDTO> catalogs;
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
  private Boolean item;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
