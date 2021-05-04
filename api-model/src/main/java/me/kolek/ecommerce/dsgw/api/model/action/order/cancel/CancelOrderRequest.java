package me.kolek.ecommerce.dsgw.api.model.action.order.cancel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CancelOrderRequest {
  private String cancelCode;
  private String cancelReason;
}
