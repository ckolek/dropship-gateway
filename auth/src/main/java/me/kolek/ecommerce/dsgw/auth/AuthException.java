package me.kolek.ecommerce.dsgw.auth;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
  private final Type type;

  public AuthException(String message, Type type) {
    super(message);
    this.type = type;
  }

  public enum Type {
    UNAUTHORIZED,
    FORBIDDEN
  }
}
