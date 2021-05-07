package me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge;

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
public class AcknowledgeOrderRequest {
  @Singular
  private List<AcknowledgeOrderItem> items;
}
