package me.kolek.ecommerce.dsgw.api.http;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.auth.AuthContext;
import me.kolek.ecommerce.dsgw.auth.AuthException;
import me.kolek.ecommerce.dsgw.auth.AuthProperties;
import me.kolek.ecommerce.dsgw.auth.token.AuthTokenGenerator;
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

    try {
      AuthContext.populate(jws);
      chain.doFilter(request, response);
    } catch (AuthException e) {
      switch (e.getType()) {
        case UNAUTHORIZED -> HttpUtils.respond(response, HttpStatus.SC_UNAUTHORIZED, e.getMessage());
        case FORBIDDEN -> HttpUtils.respond(response, HttpStatus.SC_FORBIDDEN, e.getMessage());
      }
    } finally {
      AuthContext.clear();
    }
  }
}
