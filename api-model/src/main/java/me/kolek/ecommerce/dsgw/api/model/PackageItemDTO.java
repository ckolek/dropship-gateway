package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class PackageItemDTO {

  private String id;
  private PackageDTO pkg;
  private OrderItemDTO orderItem;
  private Integer quantity;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
