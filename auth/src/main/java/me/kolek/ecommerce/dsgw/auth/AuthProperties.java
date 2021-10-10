package me.kolek.ecommerce.dsgw.auth;

import java.time.Duration;
import java.util.Set;
import lombok.Data;
import org.apache.http.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("dsgw.auth")
public class AuthProperties {

  private Duration tokenExpiration = Duration.ofDays(1);
  private boolean enabled = true;
  private String headerName = HttpHeaders.AUTHORIZATION;
  private String headerPrefix = "bearer ";
  private Set<String> urlPatterns = Set.of();

  public Duration getMaxTokenExpiration(Duration requested) {
    if (requested != null && requested.compareTo(tokenExpiration) < 0) {
      return requested;
    } else {
      return tokenExpiration;
    }
  }
}
