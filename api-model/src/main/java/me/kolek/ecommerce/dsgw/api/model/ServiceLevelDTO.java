package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class ServiceLevelDTO {
  private String id;
  private CarrierDTO carrier;
  private String mode;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
