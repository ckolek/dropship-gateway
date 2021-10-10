package me.kolek.ecommerce.dsgw.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kolek.ecommerce.dsgw.auth.AuthException.Type;
import me.kolek.ecommerce.dsgw.context.RequestContext;
import me.kolek.ecommerce.dsgw.model.Organization;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthContext {

  private static final String JWS = "jws";

  public static void populate(Jws<Claims> jws) {
    Claims claims = jws.getBody();
    RequestContext.use(requestContext -> {
      requestContext.putValue(JWS, jws);
      populateContextFromClaim(requestContext, claims, MoreClaims.SERVICE_ID, Long.class);
      populateContextFromClaim(requestContext, claims, MoreClaims.ORG_ID, Long.class);
      populateContextFromClaim(requestContext, claims, MoreClaims.ORG_TYPE, String.class);
      populateContextFromClaim(requestContext, claims, MoreClaims.USER_ID, Long.class);
      populateContextFromClaim(requestContext, claims, MoreClaims.ROLES, Collection.class,
          Set::copyOf, true);
      populateContextFromClaim(requestContext, claims, MoreClaims.SCOPES, Collection.class,
          AuthScopeSet::copyOfValues, true);
    });
  }

  private static void populateContextFromClaim(RequestContext context, Claims claims, String name,
      Class<?> claimType) {
    populateContextFromClaim(context, claims, name, claimType, Function.identity(), false);
  }

  private static <T> void populateContextFromClaim(RequestContext context, Claims claims,
      String name, Class<T> claimType, Function<T, ?> mapper, boolean required) {
    Optional.ofNullable(claims.get(name, claimType)).map(mapper).ifPresentOrElse(
        value -> context.putValue(name, value),
        () -> {
          if (required) {
            throw new NoSuchElementException("claim " + name + " not present in token");
          }
        });
  }

  public static void requireScope(String action, String... path) {
    RequestContext.use(requestContext -> {
      var scopes = requestContext.<Set<AuthScope>>getValue(MoreClaims.SCOPES)
          .orElseThrow(() -> new AuthException("request is not authenticated", Type.UNAUTHORIZED));

      var scope = new AuthScope(action, List.of(replaceScopePlaceholders(requestContext, path)));

      if (!scopes.contains(scope)) {
        throw new AuthException(scope);
      }
    });
  }

  private static String[] replaceScopePlaceholders(RequestContext requestContext, String[] path) {
    Optional<String> orgType = requestContext.getValue(MoreClaims.ORG_TYPE);

    boolean isAdmin = requestContext.containsKey(MoreClaims.SERVICE_ID) ||
        orgType.filter(Organization.Type.ADMIN.name()::equalsIgnoreCase).isPresent();

    return Arrays.stream(path).map(part -> {
      if (ScopeConstants.ORG_ID.equals(part)) {
        Optional<Long> orgId = requestContext.getValue(MoreClaims.ORG_ID);
        return orgId.filter(v -> !isAdmin).map(Object::toString).orElse(AuthScope.ANY);
      }
      if (ScopeConstants.ORG_TYPE.equals(part)) {
        return orgType.filter(v -> !isAdmin).orElse(AuthScope.ANY);
      }
      if (ScopeConstants.USER_ID.equals(part)) {
        Optional<Long> userId = requestContext.getValue(MoreClaims.USER_ID);
        return userId.filter(v -> !isAdmin).map(Object::toString).orElse(AuthScope.ANY);
      }
      return part;
    }).toArray(String[]::new);
  }

  public static void clear() {
    RequestContext.use(requestContext -> {
      requestContext.removeValue(JWS);
      requestContext.removeValue(MoreClaims.SERVICE_ID);
      requestContext.removeValue(MoreClaims.ORG_ID);
      requestContext.removeValue(MoreClaims.ORG_TYPE);
      requestContext.removeValue(MoreClaims.USER_ID);
      requestContext.removeValue(MoreClaims.ROLES);
      requestContext.removeValue(MoreClaims.SCOPES);
    });
  }
}
