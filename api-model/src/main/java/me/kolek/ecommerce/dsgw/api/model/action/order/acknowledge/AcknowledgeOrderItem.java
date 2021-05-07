package me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AcknowledgeOrderItem {
  private Integer lineNumber;
  private Integer quantityAccepted;
  private Integer quantityRejected;
  private String rejectCode;
  private String rejectReason;
}
