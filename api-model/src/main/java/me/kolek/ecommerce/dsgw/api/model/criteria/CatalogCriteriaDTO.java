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
public class CatalogCriteriaDTO {
  private String id;
  private String externalId;
  private String description;
  private List<SupplierCriteriaDTO> suppliers;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
