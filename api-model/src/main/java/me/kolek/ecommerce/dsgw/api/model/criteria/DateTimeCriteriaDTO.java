package me.kolek.ecommerce.dsgw.api.model.criteria;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DateTimeCriteriaDTO {
  private OffsetDateTime after;
  private OffsetDateTime before;
}
