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
public class InvoiceItemDTO {

  private String id;
  private InvoiceDTO invoice;
  private OrderItemDTO orderItem;
  private Integer quantity;
  private Float unitCost;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
