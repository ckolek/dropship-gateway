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
public class ServiceLevelDTO {
  private String id;
  private CarrierDTO carrier;
  private String name;
  private String code;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
