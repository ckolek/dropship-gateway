package me.kolek.ecommerce.dsgw.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CatalogItemOptionDTO extends CatalogEntryDTO {

  private CatalogItemDTO item;
}
