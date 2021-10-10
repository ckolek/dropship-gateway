package me.kolek.ecommerce.dsgw.api.controller.v1.auth;

import com.google.common.base.Splitter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.auth.ProvisionRequest;
import me.kolek.ecommerce.dsgw.api.model.auth.ProvisionResponse;
import me.kolek.ecommerce.dsgw.api.model.auth.TokenResponse;
import me.kolek.ecommerce.dsgw.auth.AuthException;
import me.kolek.ecommerce.dsgw.auth.AuthException.Type;
import me.kolek.ecommerce.dsgw.auth.AuthScope;
import me.kolek.ecommerce.dsgw.auth.AuthScopeSet;
import me.kolek.ecommerce.dsgw.auth.CredentialManager;
import me.kolek.ecommerce.dsgw.auth.token.AuthToken;
import me.kolek.ecommerce.dsgw.auth.token.AuthTokenGenerator;
import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import me.kolek.ecommerce.dsgw.model.ClientCredentials.ClientType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class AuthController {

  private static final String BASIC_PREFIX = "basic ";
  private static final String CLIENT_CREDENTIALS_GRANT_TYPE = "client_credentials";
  private static final Splitter BASIC_AUTH_SPLITTER = Splitter.on(":");
  private static final String SCOPE_SEPARATOR = " ";
  private static final Splitter SCOPE_SPLITTER = Splitter.on(SCOPE_SEPARATOR).trimResults()
      .omitEmptyStrings();

  private final CredentialManager credentialManager;
  private final AuthTokenGenerator tokenGenerator;

  @PostMapping("/provision")
  public ProvisionResponse provisionCredentials(@RequestBody ProvisionRequest request) {
    String clientSecret = UUID.randomUUID().toString();

    ClientCredentials credentials = credentialManager.provisionCredentials(request.getClientId(),
        clientSecret, ClientType.valueOf(request.getClientType()));

    return toProvisionResponse(credentials, clientSecret);
  }

  @PostMapping("/token")
  public TokenResponse getToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
      @RequestParam("grant_type") String grantType,
      @RequestParam("scope") String scope,
      @RequestParam(value = "expiration", required = false) Duration expiration) {
    Pair<String, String> credentials = parseBasicAuth(authorization);

    if (!CLIENT_CREDENTIALS_GRANT_TYPE.equals(grantType)) {
      throw new AuthException("unsupported grant_type", Type.UNAUTHORIZED);
    }

    ClientCredentials validatedCredentials = credentialManager.validateCredentials(
        credentials.getFirst(), credentials.getSecond());

    AuthToken token = tokenGenerator.generateToken(validatedCredentials, parseScope(scope),
        expiration);

    return toTokenResponse(token);
  }

  @PostMapping("/reset")
  public ProvisionResponse resetCredentials(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    Jws<Claims> jws = tokenGenerator.parseToken(authorization);

    return resetCredentialsByClientId(jws.getBody().getSubject());
  }

  @PostMapping("/reset/{clientId}")
  public ProvisionResponse resetCredentialsByClientId(@PathVariable("clientId") String clientId) {
    String clientSecret = UUID.randomUUID().toString();

    ClientCredentials credentials = credentialManager.resetCredentials(clientId, clientSecret);

    return toProvisionResponse(credentials, clientSecret);
  }

  private static Pair<String, String> parseBasicAuth(String authorization) {
    if (authorization == null || authorization.isBlank()) {
      throw new AuthException(HttpHeaders.AUTHORIZATION + " header is required", Type.UNAUTHORIZED);
    }

    if (!StringUtils.startsWithIgnoreCase(authorization, BASIC_PREFIX)) {
      throw new AuthException("invalid " + HttpHeaders.AUTHORIZATION + " header value",
          Type.UNAUTHORIZED);
    }

    authorization = authorization.substring(BASIC_PREFIX.length());
    authorization = new String(Base64.getDecoder().decode(authorization));

    List<String> parts = BASIC_AUTH_SPLITTER.splitToList(authorization);
    if (parts.size() != 2) {
      throw new AuthException("invalid " + HttpHeaders.AUTHORIZATION + " header value",
          Type.UNAUTHORIZED);
    }

    return Pair.of(parts.get(0), parts.get(1));
  }

  private Set<AuthScope> parseScope(String scope) {
    return AuthScopeSet.copyOfValues(SCOPE_SPLITTER.splitToList(scope));
  }

  private static ProvisionResponse toProvisionResponse(ClientCredentials credentials,
      String clientSecret) {
    return ProvisionResponse.builder()
        .id(credentials.getId())
        .clientId(credentials.getClientId())
        .clientSecret(clientSecret)
        .clientType(credentials.getClientType().name())
        .build();
  }

  private static TokenResponse toTokenResponse(AuthToken token) {
    return TokenResponse.builder()
        .accessToken(token.value())
        .tokenType(token.type())
        .expiresIn(Duration.between(OffsetDateTime.now(), token.expiration()).toMillis())
        .scope(String.join(SCOPE_SEPARATOR, token.scopes()))
        .build();
  }
}
