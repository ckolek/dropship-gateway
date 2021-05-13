package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SupplierDTO {

  private String id;
  private String name;
  private Status status;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<WarehouseDTO> warehouses;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;

  public enum Status {
    INACTIVE,
    ACTIVE
  }
}
