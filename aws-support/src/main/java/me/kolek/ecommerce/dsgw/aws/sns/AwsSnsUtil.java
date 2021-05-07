package me.kolek.ecommerce.dsgw.aws.sns;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.aws.exception.MissingAttributeException;

public class AwsSnsUtil {
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
