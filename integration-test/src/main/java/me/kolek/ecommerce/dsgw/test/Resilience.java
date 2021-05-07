package me.kolek.ecommerce.dsgw.test;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.time.Duration;

public class Resilience {
  private static final RetryConfig CONFIG = RetryConfig.custom()
      .maxAttempts(5)
      .waitDuration(Duration.ofSeconds(1))
      .build();
  private static final RetryRegistry REGISTRY = RetryRegistry.of(CONFIG);

  public static <E extends Exception> void retry(ThrowingRunnable<E> runnable) throws E {
    var retry = REGISTRY.retry("default");
    try {
      Retry.decorateCallable(retry, () -> {
        runnable.run();
        return null;
      }).call();
    } catch (Exception e) {
      if (e instanceof RuntimeException) {
        throw (RuntimeException) e;
      }
      throw (E) e;
    }
  }

  @FunctionalInterface
  public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;
  }
}
