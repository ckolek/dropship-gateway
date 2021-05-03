package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SupplierDTO {

  private String id;
  private String name;
  private Status status;
  private List<WarehouseDTO> warehouses;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;

  public enum Status {
    INACTIVE,
    ACTIVE
  }
}
