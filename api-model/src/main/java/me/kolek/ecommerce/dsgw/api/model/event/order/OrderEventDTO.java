package me.kolek.ecommerce.dsgw.api.model.event.order;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderEventDTO {
  private Metadata metadata;
  private Type type;
  private SubType subType;
  private OrderDTO order;

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  @Builder
  public static class Metadata {
    private String requestId;
    private String orderId;
    private Long orderVersion;
    private OffsetDateTime timeEmitted;
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
