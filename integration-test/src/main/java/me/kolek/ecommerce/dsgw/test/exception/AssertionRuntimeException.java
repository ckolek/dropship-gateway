package me.kolek.ecommerce.dsgw.test.exception;

public class AssertionRuntimeException extends RuntimeException {
  public AssertionRuntimeException(AssertionError e) {
    super(e);
  }

  @Override
  public synchronized AssertionError getCause() {
    return (AssertionError) super.getCause();
  }
}
