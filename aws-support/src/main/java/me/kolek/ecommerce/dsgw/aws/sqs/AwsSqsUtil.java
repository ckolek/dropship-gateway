package me.kolek.ecommerce.dsgw.aws.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.aws.exception.MissingAttributeException;

public class AwsSqsUtil {
  public static Optional<String> getAttribute(Map<String, String> attributes, String attributeName) {
    return Optional.ofNullable(attributes.get(attributeName));
  }

  public static <T> Optional<T> getAttribute(Map<String, String> attributes, String attributeName, Function<String, T> mapper) {
    return Optional.ofNullable(attributes.get(attributeName)).map(mapper);
  }

  public static String getRequiredAttribute(Message message, String attributeName) {
    return getRequiredAttribute(message.getMessageId(), toMap(message.getMessageAttributes()),
        attributeName);
  }

  public static String getRequiredAttribute(String messageId, Map<String, String> attributes, String attributeName) {
    return getAttribute(attributes, attributeName)
        .orElseThrow(() -> new MissingAttributeException("message_id=" + messageId, attributeName));
  }

  public static <T> T getRequiredAttribute(String messageId, Map<String, String> attributes,
      String attributeName, Function<String, T> mapper) {
    return getAttribute(attributes, attributeName, mapper)
        .orElseThrow(() -> new MissingAttributeException("message_id=" + messageId, attributeName));
  }

  public static Map<String, MessageAttributeValue> toMessageAttributeValueMap(
      Map<String, String> messageAttributes) {
    return messageAttributes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
        entry -> new MessageAttributeValue().withDataType("String")
            .withStringValue(entry.getValue())));
  }

  public static Map<String, String> toMap(Map<String, MessageAttributeValue> messageAttributes) {
    return messageAttributes.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getStringValue()));
  }
}
