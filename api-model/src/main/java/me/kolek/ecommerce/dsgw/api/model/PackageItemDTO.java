package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PackageItemDTO {

  private String id;
  private PackageDTO pkg;
  private OrderItemDTO orderItem;
  private Integer quantity;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
