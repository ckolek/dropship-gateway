package me.kolek.ecommerce.dsgw.test;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.time.Duration;
import me.kolek.ecommerce.dsgw.test.exception.AssertionRuntimeException;

public class Resilience {
  private static final RetryConfig CONFIG = RetryConfig.custom()
      .maxAttempts(30)
      .waitDuration(Duration.ofSeconds(1))
      .build();
  private static final RetryRegistry REGISTRY = RetryRegistry.of(CONFIG);

  public static <E extends Exception> void retry(ThrowingRunnable<E> runnable) throws E {
    retry(() -> {
      runnable.run();
      return null;
    });
  }

  public static <T, E extends Exception> T retry(ThrowingSupplier<T, E> supplier) throws E {
    var retry = REGISTRY.retry("foo", CONFIG);
    try {
      return Retry.decorateCallable(retry, () -> {
        try {
          return supplier.get();
        } catch (AssertionError e) {
          throw new AssertionRuntimeException(e);
        }
      }).call();
    } catch (AssertionRuntimeException e) {
      throw e.getCause();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw (E) e;
    }
  }

  @FunctionalInterface
  public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;
  }

  @FunctionalInterface
  public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
  }
}
