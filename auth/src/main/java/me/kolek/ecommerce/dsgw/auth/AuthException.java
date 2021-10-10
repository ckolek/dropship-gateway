package me.kolek.ecommerce.dsgw.auth;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
  private final Type type;
  private final AuthScope scope;

  public AuthException(String message, Type type) {
    super(message);
    this.type = type;
    this.scope = null;
  }

  public AuthException(AuthScope scope) {
    super("client is not granted scope: " + scope);
    this.type = Type.FORBIDDEN;
    this.scope = scope;
  }

  public enum Type {
    UNAUTHORIZED,
    FORBIDDEN
  }
}
