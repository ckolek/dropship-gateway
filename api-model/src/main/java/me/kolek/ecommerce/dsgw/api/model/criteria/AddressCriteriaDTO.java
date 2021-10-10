package me.kolek.ecommerce.dsgw.api.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddressCriteriaDTO {
  private String line1;
  private String line2;
  private String line3;
  private String city;
  private String state;
  private String province;
  private String postalCode;
  private String country;
}
