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
public class ServiceLevelCriteriaDTO {
  private String id;
  private List<CarrierCriteriaDTO> carriers;
  private String mode;
  private String code;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
