package me.kolek.ecommerce.dsgw.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class Settable<T> {

  private T value;
  private boolean set;

  private Settable(T value, boolean set) {
    this.value = value;
    this.set = set;
  }

  public static <T> Settable<T> unset() {
    return new Settable<>(null, false);
  }

  public static <T> Settable<T> set(T value) {
    return new Settable<>(value, true);
  }

  public T getValue() {
    if (!set) {
      throw new IllegalStateException("value has not been set");
    }
    return value;
  }

  public void setValue(T value) {
    setValue(value, false);
  }

  public void setValue(T value, boolean overwrite) {
    if (set && !overwrite) {
      throw new IllegalStateException("value has already been set");
    }
    this.value = value;
    this.set = true;
  }

  public boolean isSet() {
    return set;
  }

  public void ifSet(Consumer<T> action) {
    if (set) {
      action.accept(value);
    }
  }

  public <R> Settable<R> map(Function<T, R> mapper) {
    return set ? set(mapper.apply(value)) : unset();
  }
}
