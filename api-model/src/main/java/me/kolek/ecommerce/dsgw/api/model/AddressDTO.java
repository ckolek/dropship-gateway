package me.kolek.ecommerce.dsgw.api.model;

import lombok.Data;

@Data
public class AddressDTO {
  private String line1;
  private String line2;
  private String line3;
  private String city;
  private String state;
  private String province;
  private String postalCode;
  private String country;
}
