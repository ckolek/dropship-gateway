package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CatalogItemDTO extends CatalogEntryDTO {

  public static final String TYPE = "item";

  private CatalogDTO catalog;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<CatalogItemOptionDTO> options;
}
