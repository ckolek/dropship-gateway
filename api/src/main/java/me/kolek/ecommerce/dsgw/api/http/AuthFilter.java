package me.kolek.ecommerce.dsgw.api.http;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.auth.AuthException;
import me.kolek.ecommerce.dsgw.auth.AuthProperties;
import me.kolek.ecommerce.dsgw.auth.AuthScopeSet;
import me.kolek.ecommerce.dsgw.auth.MoreClaims;
import me.kolek.ecommerce.dsgw.auth.token.AuthTokenGenerator;
import me.kolek.ecommerce.dsgw.context.RequestContext;
import org.apache.http.HttpStatus;

@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends HttpFilter {

  public static final String JWS = "jws";

  private final String headerName;
  private final AuthTokenGenerator tokenGenerator;

  public AuthFilter(AuthProperties properties, AuthTokenGenerator tokenGenerator) {
    this(properties.getHeaderName(), tokenGenerator);
  }

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String authToken = request.getHeader(headerName);
    if (authToken == null || authToken.isBlank()) {
      HttpUtils.respond(response, HttpStatus.SC_UNAUTHORIZED, "%s header required", headerName);
      return;
    }

    Jws<Claims> jws;
    try {
      jws = tokenGenerator.parseToken(authToken);
    } catch (AuthException e) {
      HttpUtils.respond(response, HttpStatus.SC_UNAUTHORIZED, e.getMessage());
      return;
    }

    Claims claims = jws.getBody();

    RequestContext.use(requestContext -> {
      requestContext.putValue(JWS, jws);
      Optional.ofNullable(claims.get(MoreClaims.SERVICE_ID, Long.class))
          .ifPresent(serviceId -> requestContext.putValue(MoreClaims.SERVICE_ID, serviceId));
      Optional.ofNullable(claims.get(MoreClaims.ORG_ID, Long.class))
          .ifPresent(orgId -> requestContext.putValue(MoreClaims.ORG_ID, orgId));
      Optional.ofNullable(claims.get(MoreClaims.USER_ID, Long.class))
          .ifPresent(userId -> requestContext.putValue(MoreClaims.USER_ID, userId));
      requestContext.putValue(MoreClaims.ROLES,
          Set.copyOf((Collection<String>) claims.get(MoreClaims.ROLES)));
      requestContext.putValue(MoreClaims.SCOPES,
          AuthScopeSet.copyOfValues((Collection<String>) claims.get(MoreClaims.SCOPES)));
    });

    chain.doFilter(request, response);
  }
}
