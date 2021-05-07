package me.kolek.ecommerce.dsgw.internal.model.order.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.cancel.CancelOrderRequest;

@AllArgsConstructor
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AcknowledgeOrderAction extends OrderAction<AcknowledgeOrderRequest> {

  public static final String TYPE = "acknowledgeOrder";

  @Override
  public String getType() {
    return TYPE;
  }
}
