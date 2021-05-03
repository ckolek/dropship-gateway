package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CatalogItemDTO extends CatalogEntryDTO {

  private CatalogDTO catalog;
  private List<CatalogItemOptionDTO> options;
}
