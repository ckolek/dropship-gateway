package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WarehouseDTO {

  private String id;
  private SupplierDTO supplier;
  private String code;
  private String supplierCode;
  private String description;
  private Status status;
  private AddressDTO address;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;

  public enum Status {
    INACTIVE,
    ACTIVE
  }
}
