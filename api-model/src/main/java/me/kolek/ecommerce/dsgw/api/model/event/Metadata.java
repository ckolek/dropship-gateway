package me.kolek.ecommerce.dsgw.api.model.event;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Metadata {
  private String requestId;
  private String recordId;
  private Long recordVersion;
  private OffsetDateTime timeEmitted;
}
