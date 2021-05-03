package me.kolek.ecommerce.dsgw.internal.model.order.action.submit;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class SubmitOrderAction extends OrderAction<SubmitOrderRequest> {

  public static final String TYPE = "submitOrder";

  @Override
  public String getType() {
    return TYPE;
  }
}
