package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CatalogCriteriaDTO {
  private String id;
  private String externalId;
  private List<SupplierCriteriaDTO> suppliers;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
