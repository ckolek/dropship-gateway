package me.kolek.ecommerce.dsgw.api.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionResponse {

  private Long id;
  private String clientId;
  private String clientSecret;
  private String clientType;
}