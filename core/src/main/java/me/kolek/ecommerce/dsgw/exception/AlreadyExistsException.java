package me.kolek.ecommerce.dsgw.exception;

public class AlreadyExistsException extends RuntimeException {

  private final Class<?> entityClass;
  private final String field;
  private final Object value;

  public AlreadyExistsException(Class<?> entityClass, String field, Object value) {
    super(createMessage(entityClass, field, value));
    this.entityClass = entityClass;
    this.field = field;
    this.value = value;
  }

  private static String createMessage(Class<?> entityClass, String field, Object value) {
    return entityClass.getSimpleName() + " already exists: " + field + "=" + value;
  }
}
