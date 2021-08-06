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
public class WarehouseCriteriaDTO {

  private String id;
  private List<SupplierCriteriaDTO> suppliers;
  private String code;
  private String supplierCode;
  private String description;
  private List<WarehouseDTO.Status> statuses;
  private AddressCriteriaDTO address;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
