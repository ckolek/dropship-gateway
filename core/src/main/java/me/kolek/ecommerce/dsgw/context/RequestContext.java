package me.kolek.ecommerce.dsgw.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestContext implements AutoCloseable {

  private static final ThreadLocal<RequestContext> CURRENT = new ThreadLocal<>();

  @Getter
  private final String id;
  private final Map<String, Object> values = new HashMap<>();

  public boolean containsKey(String key) {
    return values.containsKey(key);
  }

  public <T> Optional<T> getValue(String key) {
    return Optional.ofNullable((T) values.get(key));
  }

  public <T> Optional<T> putValue(String key, T value) {
    return Optional.ofNullable((T) values.put(key, value));
  }

  public <T> Optional<T> putValueIfAbsent(String key, T value) {
    return Optional.ofNullable((T) values.putIfAbsent(key, value));
  }

  public <T> Optional<T> removeValue(String key) {
    return Optional.ofNullable((T) values.remove(key));
  }

  public void clearValues() {
    values.clear();
  }

  @Override
  public void close() {
    if (CURRENT.get() != this) {
      throw new IllegalStateException(
          "request context must be closed in same thread in which it was initialized");
    }
    CURRENT.set(null);
  }

  private static String generateId() {
    return UUID.randomUUID().toString();
  }

  public static RequestContext initialize() {
    return initialize(generateId());
  }

  public static RequestContext initialize(String requestId) {
    return initialize(requestId, false);
  }

  public static RequestContext initialize(String requestId, boolean force) {
    RequestContext requestContext = CURRENT.get();
    if (requestContext != null && !force) {
      throw new IllegalStateException("request context has already been initialized");
    }
    if (requestId == null) {
      requestId = generateId();
    }
    CURRENT.set(requestContext = new RequestContext(requestId));
    return requestContext;
  }

  public static Optional<RequestContext> getOptionally() {
    return Optional.ofNullable(CURRENT.get());
  }

  public static RequestContext get() {
    return getOptionally()
        .orElseThrow(() -> new IllegalStateException("request context has not been initialized"));
  }

  public static void use(Consumer<RequestContext> action) {
    action.accept(get());
  }

  public static void ifPresent(Consumer<RequestContext> action) {
    getOptionally().ifPresent(action);
  }
}
