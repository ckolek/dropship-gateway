package me.kolek.ecommerce.dsgw.auth.token;

import io.jsonwebtoken.JwtBuilder;
import me.kolek.ecommerce.dsgw.auth.AuthScope;
import me.kolek.ecommerce.dsgw.model.Organization;
import me.kolek.ecommerce.dsgw.model.User;

public abstract class AuthTokenBuilder {

  private static final String ORG_ID_PLACEHOLDER = "{orgId}";
  private static final String ORG_TYPE_PLACEHOLDER = "{orgType}";
  private static final String USER_ID_PLACEHOLDER = "{userId}";

  public abstract String populateScope(String scope);

  public abstract JwtBuilder augmentToken(JwtBuilder token);

  protected static String replacePlaceholders(String scope, Organization organization,
      User user) {
    scope = replaceOrgId(scope, organization);
    scope = replaceOrgType(scope, organization);
    scope = replaceUserId(scope, user);
    return scope;
  }

  protected static String replaceOrgId(String scope, Organization organization) {
    return replace(scope, ORG_ID_PLACEHOLDER,
        organization != null ? organization.getCredentials().getId() : null);
  }

  protected static String replaceOrgType(String scope, Organization organization) {
    return replace(scope, ORG_TYPE_PLACEHOLDER,
        organization != null ? organization.getType() : null);
  }

  protected static String replaceUserId(String scope, User user) {
    return replace(scope, USER_ID_PLACEHOLDER, user != null ? user.getCredentials().getId() : null);
  }

  protected static String replace(String scope, String placeholder, Object value) {
    return scope.replace(placeholder,
        value != null ? value.toString().toLowerCase() : AuthScope.ANY);
  }
}
