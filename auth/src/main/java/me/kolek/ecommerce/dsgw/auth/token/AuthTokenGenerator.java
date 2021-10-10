package me.kolek.ecommerce.dsgw.auth.token;

import com.google.common.collect.Sets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Date;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.auth.AuthException;
import me.kolek.ecommerce.dsgw.auth.AuthException.Type;
import me.kolek.ecommerce.dsgw.auth.AuthProperties;
import me.kolek.ecommerce.dsgw.auth.AuthScope;
import me.kolek.ecommerce.dsgw.auth.AuthScopeSet;
import me.kolek.ecommerce.dsgw.auth.MoreClaims;
import me.kolek.ecommerce.dsgw.exception.NotFoundException;
import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import me.kolek.ecommerce.dsgw.model.Organization;
import me.kolek.ecommerce.dsgw.model.Role;
import me.kolek.ecommerce.dsgw.model.Scope;
import me.kolek.ecommerce.dsgw.model.Service;
import me.kolek.ecommerce.dsgw.model.User;
import me.kolek.ecommerce.dsgw.repository.OrganizationRepository;
import me.kolek.ecommerce.dsgw.repository.ServiceRepository;
import me.kolek.ecommerce.dsgw.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
@Slf4j
public class AuthTokenGenerator {

  private final ServiceRepository serviceRepository;
  private final OrganizationRepository organizationRepository;
  private final UserRepository userRepository;
  private final AuthProperties authProperties;
  private final PublicKey publicKey;
  private final PrivateKey privateKey;

  public AuthToken generateToken(ClientCredentials credentials, Set<AuthScope> requestedScopes,
      Duration expiration) {
    AuthTokenBuilder tokenBuilder = switch (credentials.getClientType()) {
      case SERVICE -> createServiceTokenBuilder(credentials);
      case ORGANIZATION -> createOrganizationTokenBuilder(credentials);
      case USER -> createUserTokenBuilder(credentials);
    };

    validateRequestedScopes(credentials, requestedScopes, tokenBuilder);

    OffsetDateTime issuedAt = OffsetDateTime.now();
    OffsetDateTime expiresAt = issuedAt.plus(authProperties.getMaxTokenExpiration(expiration));

    Set<String> roleNames = collectRoleNames(credentials);
    Set<String> scopeValues = collectScopeValues(requestedScopes);

    String token = Jwts.builder()
        .setSubject(credentials.getClientId())
        .setIssuer("me.kolek.ecommerce.dsgw")
        .setAudience("me.kolek.ecommerce.dsgw")
        .setIssuedAt(Date.from(issuedAt.toInstant()))
        .setExpiration(Date.from(expiresAt.toInstant()))
        .claim(MoreClaims.ROLES, roleNames)
        .claim(MoreClaims.SCOPES, scopeValues)
        .signWith(privateKey)
        .compact();

    return new AuthToken(token, authProperties.getHeaderPrefix().strip().toLowerCase(), expiresAt,
        scopeValues);
  }

  public String stripPrefix(String token) {
    if (!StringUtils.startsWithIgnoreCase(token, authProperties.getHeaderPrefix())) {
      throw new AuthException("invalid " + authProperties.getHeaderName() + " header value",
          Type.UNAUTHORIZED);
    }
    return token.substring(authProperties.getHeaderPrefix().length());
  }

  public Jws<Claims> parseToken(String token) {
    return parseToken(true, token);
  }

  public Jws<Claims> parseToken(boolean withPrefix, String token) {
    if (withPrefix) {
      token = stripPrefix(token);
    }

    JwtParser parser = Jwts.parserBuilder()
        .setSigningKey(publicKey)
        .build();

    try {
      return parser.parseClaimsJws(token);
    } catch (Exception e) {
      log.error("invalid auth token", e);
      throw new AuthException("invalid auth token", Type.UNAUTHORIZED);
    }
  }

  public String refreshToken(String token, Duration expiration) {
    return refreshToken(true, token, expiration);
  }

  public String refreshToken(boolean withPrefix, String token, Duration expiration) {
    Jws<Claims> jws = parseToken(withPrefix, token);

    OffsetDateTime issuedAt = OffsetDateTime.now();
    OffsetDateTime expiresAt = issuedAt.plus(authProperties.getMaxTokenExpiration(expiration));

    return Jwts.builder()
        .addClaims(jws.getBody())
        .setIssuedAt(Date.from(issuedAt.toInstant()))
        .setExpiration(Date.from(expiresAt.toInstant()))
        .signWith(privateKey)
        .compact();
  }

  private ServiceTokenBuilder createServiceTokenBuilder(ClientCredentials credentials) {
    var service = serviceRepository.findById(credentials.getId()).orElseThrow(
        () -> new NotFoundException(Service.class, "clientId", credentials.getClientId()));
    return new ServiceTokenBuilder(service);
  }

  private OrganizationTokenBuilder createOrganizationTokenBuilder(ClientCredentials credentials) {
    var organization = organizationRepository.findById(credentials.getId()).orElseThrow(
        () -> new NotFoundException(Organization.class, "clientId", credentials.getClientId()));
    return new OrganizationTokenBuilder(organization);
  }
  
  private UserTokenBuilder createUserTokenBuilder(ClientCredentials credentials) {
    var user = userRepository.findById(credentials.getId()).orElseThrow(
        () -> new NotFoundException(User.class, "clientId", credentials.getClientId()));
    return new UserTokenBuilder(user);
  }

  private static void validateRequestedScopes(ClientCredentials credentials,
      Set<AuthScope> requestedScopes, AuthTokenBuilder tokenBuilder) {
    var scopes = collectScopes(credentials, tokenBuilder);
    var difference = Sets.difference(requestedScopes, scopes);
    if (!difference.isEmpty()) {
      throw new AuthException("client is not authorized to access requested scopes: " + difference,
          Type.UNAUTHORIZED);
    }
  }

  private static Set<AuthScope> collectScopes(ClientCredentials credentials,
      AuthTokenBuilder tokenBuilder) {
    return credentials.getRoles().stream()
        .flatMap(role -> role.getScopes().stream())
        .map(Scope::getValue)
        .map(tokenBuilder::populateScope)
        .map(AuthScope::parse)
        .collect(Collectors.toCollection(AuthScopeSet::new));
  }

  private static Set<String> collectScopeValues(Set<AuthScope> scopes) {
    return scopes.stream().map(AuthScope::toString).collect(Collectors.toSet());
  }

  private static Set<String> collectRoleNames(ClientCredentials credentials) {
    return credentials.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
  }
}
