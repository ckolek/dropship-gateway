package me.kolek.ecommerce.dsgw.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

  private final Class<?> entityClass;
  private final String field;
  private final Object value;

  public NotFoundException(Class<?> entityClass, String field, Object value) {
    super(createMessage(entityClass, field, value));
    this.entityClass = entityClass;
    this.field = field;
    this.value = value;
  }

  private static String createMessage(Class<?> entityClass, String field, Object value) {
    return entityClass.getSimpleName() + " not found: " + field + "=" + value;
  }
}
