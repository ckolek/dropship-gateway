package me.kolek.ecommerce.dsgw.worker.processor;

import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.getAttribute;
import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.toMap;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.aws.sqs.MessageProcessingResult;
import me.kolek.ecommerce.dsgw.aws.sqs.MessageProcessor;
import me.kolek.ecommerce.dsgw.context.RequestContext;
import me.kolek.ecommerce.dsgw.internal.model.queue.MessageAttributes;

@RequiredArgsConstructor
public abstract class ObjectMappingMessageProcessor<T> implements MessageProcessor {
  protected final ObjectMapper objectMapper;
  private final TypeReference<T> mappedType;

  @Override
  public MessageProcessingResult processMessage(Message message) throws Exception {
    Map<String, String> messageAttributes = toMap(message.getMessageAttributes());

    String requestId = getAttribute(messageAttributes, MessageAttributes.REQUEST_ID).orElse(null);

    try (var requestContext = RequestContext.initialize(requestId)) {
      return processMessage(message.getMessageId(),
          objectMapper.readValue(message.getBody(), mappedType),
          messageAttributes);
    }
  }

  protected abstract MessageProcessingResult processMessage(String messageId, T body,
      Map<String, String> attributes) throws Exception;
}
