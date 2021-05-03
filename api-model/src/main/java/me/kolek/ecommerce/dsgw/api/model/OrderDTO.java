package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDTO {
  private String id;
  private String orderNumber;
  private String customerOrderNumber;
  private WarehouseDTO warehouse;
  private ContactDTO contact;
  private AddressDTO address;
  private List<OrderItemDTO> items;
  private ServiceLevelDTO serviceLevel;
  private Status status;
  private OffsetDateTime timeOrdered;
  private OffsetDateTime timeReleased;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;

  public enum Status {
    NEW,
    ACKNOWLEDGED,
    SHIPPED_PARTIAL,
    SHIPPED,
    INVOICED,
    CANCELLED
  }
}
