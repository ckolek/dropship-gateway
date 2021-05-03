package me.kolek.ecommerce.dsgw.api.model.action.order.submit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SubmitOrderRecipient {
  private String name;
  private String phone;
  private String email;
  private String line1;
  private String line2;
  private String line3;
  private String city;
  private String state;
  private String province;
  private String postalCode;
  private String country;
}