package me.kolek.ecommerce.dsgw.api.model.action.order.ship;

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
public class OrderShipmentRequest {
  private String manifestId;
  private String warehouseCode;
  private OrderShipmentCorrespondent sender;
  private OrderShipmentCorrespondent recipient;
  private String carrierName;
  private String carrierMode;
  private String carrierServiceLevelCode;
  private String trackingNumber;
  private List<OrderShipmentItem> items;
  private OffsetDateTime timeShipped;
}
