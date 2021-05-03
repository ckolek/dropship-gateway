package me.kolek.ecommerce.dsgw.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

  @Column(name = "line1", nullable = false, length = 64)
  private String line1;

  @Column(name = "line2", length = 64)
  private String line2;

  @Column(name = "line3", length = 64)
  private String line3;

  @Column(name = "city", nullable = false, length = 64)
  private String city;

  @Column(name = "state", length = 64)
  private String state;

  @Column(name = "province", length = 64)
  private String province;

  @Column(name = "postal_code", nullable = false, length = 64)
  private String postalCode;

  @Column(name = "country", nullable = false, length = 3)
  private String country;
}
