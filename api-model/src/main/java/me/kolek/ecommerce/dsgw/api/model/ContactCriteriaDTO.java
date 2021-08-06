package me.kolek.ecommerce.dsgw.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContactCriteriaDTO {
  private String name;
  private String phone;
  private String email;
}
