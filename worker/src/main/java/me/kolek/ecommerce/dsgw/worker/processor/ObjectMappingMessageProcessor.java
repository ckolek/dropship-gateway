package me.kolek.ecommerce.dsgw.worker.processor;

import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.toMap;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.aws.sqs.MessageProcessingResult;
import me.kolek.ecommerce.dsgw.aws.sqs.MessageProcessor;

@RequiredArgsConstructor
public abstract class ObjectMappingMessageProcessor<T> implements MessageProcessor {
  protected final ObjectMapper objectMapper;
  private final TypeReference<T> mappedType;

  @Override
  public MessageProcessingResult processMessage(Message message) throws Exception {
    return processMessage(message.getMessageId(),
        objectMapper.readValue(message.getBody(), mappedType),
        toMap(message.getMessageAttributes()));
  }

  protected abstract MessageProcessingResult processMessage(String messageId, T body,
      Map<String, String> attributes) throws Exception;
}
