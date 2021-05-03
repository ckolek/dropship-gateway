package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CarrierDTO {
  private String id;
  private String name;
  private List<ServiceLevelDTO> serviceLevels;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
