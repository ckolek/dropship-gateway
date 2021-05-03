package me.kolek.ecommerce.dsgw.internal.model.order.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.internal.model.order.action.submit.SubmitOrderAction;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonTypeInfo(
    use = Id.NAME,
    include = As.EXISTING_PROPERTY,
    property = "type",
    defaultImpl = OrderAction.class
)
@JsonSubTypes({
    @Type(name = SubmitOrderAction.TYPE, value = SubmitOrderAction.class)
})
public abstract class OrderAction<T> {
  private String orderId;
  private T request;

  public abstract String getType();

  public OrderActionResult.OrderActionResultBuilder toResultBuilder() {
    return OrderActionResult.builder().orderId(orderId);
  }

  public OrderActionResult.OrderActionResultBuilder toResultBuilder(
      OrderActionResult.Status status) {
    return toResultBuilder().status(status);
  }
}
