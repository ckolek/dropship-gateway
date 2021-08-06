package me.kolek.ecommerce.dsgw.api.model.action.order.ship;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderShipmentItem {
  private Integer orderLineNumber;
  private Integer quantity;
}
