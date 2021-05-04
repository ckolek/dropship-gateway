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
public class OrderItemDTO {
  private String id;
  private OrderDTO order;
  private Integer lineNumber;
  private CatalogEntryDTO catalogEntry;
  private Integer quantity;
  private String customization;
  private OrderCancelCodeDTO cancelCode;
  private String cancelReason;
  private OffsetDateTime expectedShipDate;
  private OffsetDateTime expectedDeliveryDate;
  private OffsetDateTime timeCancelled;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
