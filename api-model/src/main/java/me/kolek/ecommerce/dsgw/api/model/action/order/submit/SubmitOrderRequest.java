package me.kolek.ecommerce.dsgw.api.model.action.order.submit;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SubmitOrderRequest {
  private String orderNumber;
  private String customerOrderNumber;
  private String warehouseCode;
  private SubmitOrderRecipient recipient;
  @Singular
  private List<SubmitOrderItem> items;
  private String carrierName;
  private String carrierMode;
  private OffsetDateTime timeOrdered;
  private OffsetDateTime timeReleased;
}
