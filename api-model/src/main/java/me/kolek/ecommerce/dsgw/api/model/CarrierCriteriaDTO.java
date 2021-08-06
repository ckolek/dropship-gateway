package me.kolek.ecommerce.dsgw.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarrierCriteriaDTO {
  private String id;
  private String name;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
