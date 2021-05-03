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
public class Contact {

  @Column(name = "name", length = 64)
  private String name;

  @Column(name = "phone", length = 16)
  private String phone;

  @Column(name = "email", length = 64)
  private String email;
}
