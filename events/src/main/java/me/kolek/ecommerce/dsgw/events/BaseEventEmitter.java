package me.kolek.ecommerce.dsgw.events;

import static me.kolek.ecommerce.dsgw.aws.sns.AwsSnsUtil.toMessageAttributeValueMap;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.api.model.event.EventDTO;
import me.kolek.ecommerce.dsgw.internal.model.queue.MessageAttributes;

@RequiredArgsConstructor
@Slf4j
public abstract class BaseEventEmitter {

  private final ObjectMapper objectMapper;
  private final AmazonSNS sns;

  protected void emitEvent(EventDTO<?> event, String topicArn) throws IOException {
    String message = objectMapper.writeValueAsString(event);

    log.debug("emitting event to topic {}: {}", topicArn, message);

    Map<String, String> messageAttributes = new HashMap<>();
    messageAttributes.put(MessageAttributes.RECORD_ID, event.getMetadata().getRecordId());
    messageAttributes.put(MessageAttributes.EVENT_TYPE, event.getTypeName());
    event.getSubTypeName().ifPresent(subTypeName ->
        messageAttributes.put(MessageAttributes.EVENT_SUB_TYPE, subTypeName));

    sns.publish(new PublishRequest()
        .withTopicArn(topicArn)
        .withMessageAttributes(toMessageAttributeValueMap(messageAttributes))
        .withMessage(message));
  }
}
