package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
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
  private OffsetDateTime expectedShipDate;
  private OffsetDateTime expectedDeliveryDate;
  private OrderDTO.Status status;
  private Integer quantityAccepted;
  private Integer quantityRejected;
  private OrderCancelCodeDTO rejectCode;
  private String rejectReason;
  private OffsetDateTime timeAcknowledged;
  private Integer quantityCancelled;
  private OrderCancelCodeDTO cancelCode;
  private String cancelReason;
  private OffsetDateTime timeCancelled;
  private List<PackageItemDTO> packageItems;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
