package me.kolek.ecommerce.dsgw.api.model.action.order.submit;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SubmitOrderRequest {
  private String orderNumber;
  private String customerOrderNumber;
  private String warehouseCode;
  private SubmitOrderRecipient recipient;
  @Singular
  private List<SubmitOrderItem> items;
  private String carrierName;
  private String carrierMode;
  private String carrierServiceLevelCode;
  private OffsetDateTime timeOrdered;
  private OffsetDateTime timeReleased;
}
