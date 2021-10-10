package me.kolek.ecommerce.dsgw.test;

import io.cucumber.java.en.Given;
import java.util.Set;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.auth.ProvisionResponse;
import me.kolek.ecommerce.dsgw.api.model.auth.TokenResponse;
import me.kolek.ecommerce.dsgw.test.api.AuthApi;

@RequiredArgsConstructor
public class Auth {

  private static final Set<String> ADMIN_SCOPES = Set.of("*:org/*/*", "*:/org/*/*/user/*");

  private final AuthApi authApi;
  private final TokenHolder tokenHolder;

  public void authenticateAs(String clientId, Set<String> scopes) throws Exception {
    ProvisionResponse resetResponse = authApi.resetCredentials(clientId);
    tokenHolder.set(authApi.getToken(clientId, resetResponse.getClientSecret(), scopes));
  }

  @Given("Client is authenticated as admin organization")
  public void authenticatedAsAdminOrg() throws Exception {
    authenticateAs("admin", ADMIN_SCOPES);
  }

  @Given("Client is unauthenticated")
  public void unauthenticate() {
    tokenHolder.clear();
  }

  public static class TokenHolder {
    private String tokenValue;
    private String tokenType;

    public boolean isSet() {
      return tokenValue != null;
    }

    public void ifSet(BiConsumer<String, String> action) {
      if (isSet()) {
        action.accept(tokenValue, tokenType);
      }
    }

    public void set(TokenResponse response) {
      this.tokenValue = response.getAccessToken();
      this.tokenType = response.getTokenType();
    }

    public void clear() {
      this.tokenValue = null;
      this.tokenType = null;
    }
  }
}
