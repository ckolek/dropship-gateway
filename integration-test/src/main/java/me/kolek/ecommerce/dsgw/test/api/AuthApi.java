package me.kolek.ecommerce.dsgw.test.api;

import java.net.URLEncoder;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.api.model.auth.ProvisionRequest;
import me.kolek.ecommerce.dsgw.api.model.auth.ProvisionResponse;
import me.kolek.ecommerce.dsgw.api.model.auth.TokenResponse;
import me.kolek.ecommerce.dsgw.test.Auth;
import me.kolek.ecommerce.dsgw.test.Json;

public class AuthApi extends HttpApi {

  public AuthApi(Json json, Auth.TokenHolder tokenHolder) {
    super(json, tokenHolder);
  }

  public ProvisionResponse provisionCredentials(String clientId, String clientType)
      throws Exception {
    var request = ProvisionRequest.builder().clientId(clientId).clientType(clientType).build();
    return post("v1/auth/provision", request, ProvisionResponse.class).body();
  }

  public TokenResponse getToken(String clientId, String clientSecret, Set<String> scopes)
      throws Exception {
    String payload = Map.of(
        "grant_type", "client_credentials",
        "scope", String.join(" ", scopes)).entrySet().stream()
        .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));

    String authorization = Base64.getEncoder()
        .encodeToString((clientId + ":" + clientSecret).getBytes());

    return invoke("v1/auth/token", request -> request
            .POST(BodyPublishers.ofString(payload))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Accept", "application/json")
            .header("Authorization", "basic " + authorization),
        toTypeReference(TokenResponse.class)).body();
  }

  public ProvisionResponse resetCredentials(String clientId) throws Exception {
    return post("v1/auth/reset/" + clientId, null, ProvisionResponse.class).body();
  }
}
