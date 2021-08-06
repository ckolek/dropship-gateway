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
public class OrderDTO {
  private String id;
  private String orderNumber;
  private String customerOrderNumber;
  private WarehouseDTO warehouse;
  private RecipientDTO recipient;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<OrderItemDTO> items;
  private ServiceLevelDTO serviceLevel;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<PackageDTO> packages;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<InvoiceDTO> invoices;
  private Status status;
  private OrderCancelCodeDTO cancelCode;
  private String cancelReason;
  private OffsetDateTime timeOrdered;
  private OffsetDateTime timeReleased;
  private OffsetDateTime timeAcknowledged;
  private OffsetDateTime timeCancelled;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;

  public enum Status {
    NEW,
    ACKNOWLEDGED,
    SHIPPED_PARTIAL,
    SHIPPED,
    INVOICED_PARTIAL,
    INVOICED,
    CANCELLED
  }
}
