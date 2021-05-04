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
public class OrderCancelCodeDTO {
  private String id;
  private String code;
  private String description;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
