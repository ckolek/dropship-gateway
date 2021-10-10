package me.kolek.ecommerce.dsgw.auth.token;

import io.jsonwebtoken.JwtBuilder;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.auth.MoreClaims;
import me.kolek.ecommerce.dsgw.model.Service;

@RequiredArgsConstructor
public class ServiceTokenBuilder extends AuthTokenBuilder {
  private final Service service;

  @Override
  public String populateScope(String scope) {
    return replacePlaceholders(scope, null, null);
  }

  @Override
  public JwtBuilder augmentToken(JwtBuilder token) {
    return token.claim(MoreClaims.SERVICE_ID, service.getCredentials().getId());
  }
}
