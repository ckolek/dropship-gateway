package me.kolek.ecommerce.dsgw.auth.token;

import io.jsonwebtoken.JwtBuilder;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.auth.MoreClaims;
import me.kolek.ecommerce.dsgw.model.Organization;

@RequiredArgsConstructor
public class OrganizationTokenBuilder extends AuthTokenBuilder {
  private final Organization organization;

  @Override
  public String populateScope(String scope) {
    return replacePlaceholders(scope, organization, null);
  }

  @Override
  public JwtBuilder augmentToken(JwtBuilder token) {
    return token
        .claim(MoreClaims.ORG, organization.getName())
        .claim(MoreClaims.ORG_ID, organization.getCredentials().getId());
  }
}
