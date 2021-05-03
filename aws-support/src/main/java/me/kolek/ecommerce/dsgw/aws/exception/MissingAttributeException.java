package me.kolek.ecommerce.dsgw.aws.exception;

public class MissingAttributeException extends RuntimeException {
  public MissingAttributeException(String identifier, String attributeName) {
    super("required attribute not found: " + identifier + ", attribute_name=" + attributeName);
  }
}
