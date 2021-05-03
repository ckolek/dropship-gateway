package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class OrderItemDTO {
  private String id;
  private OrderDTO order;
  private Integer lineNumber;
  private CatalogEntryDTO catalogEntry;
  private Integer quantity;
  private String customization;
  private OffsetDateTime expectedShipDate;
  private OffsetDateTime expectedDeliveryDate;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
