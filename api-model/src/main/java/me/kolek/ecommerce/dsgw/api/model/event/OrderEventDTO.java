package me.kolek.ecommerce.dsgw.api.model.event;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class OrderEventDTO extends EventDTO<OrderDTO> {
  private Type type;
  private SubType subType;

  @Override
  public String getTypeName() {
    return type.name();
  }

  @Override
  public Optional<String> getSubTypeName() {
    return Optional.ofNullable(subType).map(Enum::name);
  }

  public enum Type {
    ORDER_CREATED,
    ORDER_ACKNOWLEDGED,
    ORDER_CANCELLED,
    ORDER_SHIPPED_PARTIAL,
    ORDER_SHIPPED,
    ORDER_INVOICED_PARTIAL,
    ORDER_INVOICED
  }

  public enum SubType {
  }
}
