package me.kolek.ecommerce.dsgw.auth;

import com.google.common.base.Splitter;
import java.util.List;

public record AuthScope(String action, List<String> path) {

  public static final String ANY = "*";

  private static final String SCOPE_SEPARATOR = ":";
  private static final String PATH_SEPARATOR = "/";
  private static final Splitter SCOPE_SPLITTER = Splitter.on(SCOPE_SEPARATOR).trimResults()
      .omitEmptyStrings();
  private static final Splitter PATH_SPLITTER = Splitter.on(PATH_SEPARATOR).trimResults()
      .omitEmptyStrings();

  @Override
  public String toString() {
    return action + SCOPE_SEPARATOR + String.join(PATH_SEPARATOR, path);
  }

  public static AuthScope parse(String scope) {
    List<String> parts = SCOPE_SPLITTER.splitToList(scope);
    if (parts.size() != 2) {
      throw new IllegalArgumentException("invalid scope - 2 parts expected");
    }

    String action = parts.get(0);
    List<String> path = PATH_SPLITTER.splitToList(parts.get(1));

    return new AuthScope(action, List.copyOf(path));
  }
}
