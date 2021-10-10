package me.kolek.ecommerce.dsgw.api.model.criteria;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO.Status;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SupplierCriteriaDTO {

  private String id;
  private String name;
  private List<Status> statuses;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
