package me.kolek.ecommerce.dsgw.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class AuthScopeSetTest {

  private static final AuthScope SCOPE1 = AuthScope.parse("org/*/*:*");
  private static final AuthScope SCOPE2 = AuthScope.parse("org/*/*:read");
  private static final AuthScope SCOPE3 = AuthScope.parse("org/*/*:write");
  private static final AuthScope SCOPE4 = AuthScope.parse("org/*/*/user/*:*");
  private static final AuthScope SCOPE5 = AuthScope.parse("org/*/*/user/*:read");
  private static final AuthScope SCOPE6 = AuthScope.parse("org/*/*/user/*:write");

  @Test
  public void testAnyAction() {
    Set<AuthScope> scopes = new AuthScopeSet();
    Collections.addAll(scopes, SCOPE1, SCOPE4);

    assertThat(scopes).hasSize(2);
    assertThat(scopes.contains(AuthScope.parse("org/admin/123:read"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/retail/123:write"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/supply/123:foo"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/user/abc:read"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/retail/123/user/abc:write"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/supply/123/user/abc:foo"))).isTrue();

    assertThat(scopes.contains(AuthScope.parse("org/123:read"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/123:write"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/user:read"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/user:write"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/abc:read"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/abc:write"))).isFalse();

    assertThat(scopes.contains(new AuthScope("", List.of()))).isFalse();
  }

  @Test
  public void testExplicitAction() {
    Set<AuthScope> scopes = new AuthScopeSet();
    Collections.addAll(scopes, SCOPE2, SCOPE3, SCOPE5, SCOPE6);

    assertThat(scopes).hasSize(4);
    assertThat(scopes.contains(AuthScope.parse("org/admin/123:read"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/retail/123:write"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/supply/123:foo"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/user/abc:read"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/retail/123/user/abc:write"))).isTrue();
    assertThat(scopes.contains(AuthScope.parse("org/supply/123/user/abc:foo"))).isFalse();

    assertThat(scopes.contains(AuthScope.parse("org/123:read"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/123:write"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/user:read"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/user:write"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/abc:read"))).isFalse();
    assertThat(scopes.contains(AuthScope.parse("org/admin/123/abc:write"))).isFalse();

    assertThat(scopes.contains(new AuthScope("", List.of()))).isFalse();
  }

  @Test
  public void testToArray() {
    Set<AuthScope> scopes = new AuthScopeSet();
    Collections.addAll(scopes, SCOPE1, SCOPE2, SCOPE3, SCOPE4, SCOPE5, SCOPE6);

    assertThat(scopes).hasSize(6);

    AuthScope[] array = scopes.toArray(new AuthScope[0]);
    assertThat(array).hasSameSizeAs(scopes);
    assertThat(array).containsExactlyInAnyOrder(SCOPE1, SCOPE2, SCOPE3, SCOPE4, SCOPE5, SCOPE6);
  }
}
