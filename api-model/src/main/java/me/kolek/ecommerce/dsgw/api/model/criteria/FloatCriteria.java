package me.kolek.ecommerce.dsgw.api.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FloatCriteria {
  private Float min;
  private Float max;
}
