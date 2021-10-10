package me.kolek.ecommerce.dsgw.auth.token;

import io.jsonwebtoken.JwtBuilder;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.auth.MoreClaims;
import me.kolek.ecommerce.dsgw.model.User;

@RequiredArgsConstructor
public class UserTokenBuilder extends AuthTokenBuilder {
  private final User user;

  @Override
  public String populateScope(String scope) {
    return replacePlaceholders(scope, user.getOrganization(), user);
  }

  @Override
  public JwtBuilder augmentToken(JwtBuilder token) {
    return token
        .claim(MoreClaims.ORG, user.getOrganization().getName())
        .claim(MoreClaims.ORG_ID, user.getOrganization().getCredentials().getId())
        .claim(MoreClaims.ORG_TYPE, user.getOrganization().getType().name().toLowerCase())
        .claim(MoreClaims.NAME, user.getName())
        .claim(MoreClaims.USER_ID, user.getCredentials().getId());
  }
}
