package me.kolek.ecommerce.dsgw.worker.processor;

import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.getAttribute;
import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.getRequiredAttribute;
import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.toMessageAttributeValueMap;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.aws.sqs.MessageProcessingResult;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;
import me.kolek.ecommerce.dsgw.internal.model.queue.MessageAttributes;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderActionMessageProcessor extends ObjectMappingMessageProcessor<OrderAction> {

  private final Map<String, OrderActionProcessor<?>> orderActionProcessors;
  private final AmazonSQS sqs;

  @Inject
  public OrderActionMessageProcessor(ObjectMapper objectMapper,
      Collection<OrderActionProcessor<?>> orderActionProcessors, AmazonSQS sqs) {
    super(objectMapper, new TypeReference<OrderAction>() {
    });
    this.orderActionProcessors = orderActionProcessors.stream()
        .collect(Collectors.toMap(OrderActionProcessor::getActionType, Function.identity()));
    this.sqs = sqs;
  }

  @Override
  public String getQueueLabel() {
    return "order-actions";
  }

  @Override
  protected MessageProcessingResult processMessage(String messageId, OrderAction body,
      Map<String, String> attributes) throws Exception {
    log.info("processing order action: message_id={}, action_type={}, order_id={}", messageId,
        body.getType(), body.getOrderId());

    OrderActionResult result = null;
    try {
      result = process(body);
    } catch (Exception e) {
      log.error("failed to process order action");
      result = body.toResultBuilder(Status.FAILED).build();
      throw e;
    } finally {
      if (result != null) {
        sendResult(messageId, attributes, result);
      }
    }

    return new MessageProcessingResult(false);
  }

  private OrderActionResult process(OrderAction action) throws Exception {
    OrderActionProcessor orderActionProcessor = orderActionProcessors.get(action.getType());

    if (orderActionProcessor != null) {
      return orderActionProcessor.process(action);
    } else {
      log.warn("no processor for order action: action_type={}", action.getType());
      return action.toResultBuilder(Status.FAILED).build();
    }
  }

  private void sendResult(String messageId, Map<String, String> attributes,
      OrderActionResult result) throws Exception {
    String requestId = getRequiredAttribute(messageId, attributes, MessageAttributes.REQUEST_ID);
    Optional<String> responseQueueUrl = getAttribute(attributes,
        MessageAttributes.RESPONSE_QUEUE_URL);

    if (responseQueueUrl.isEmpty()) {
      return;
    }

    String body = objectMapper.writeValueAsString(result);

    Map<String, String> messageAttributes = Map.of(MessageAttributes.REQUEST_ID, requestId);

    sqs.sendMessage(new SendMessageRequest()
        .withQueueUrl(responseQueueUrl.get())
        .withMessageBody(body)
        .withMessageAttributes(toMessageAttributeValueMap(messageAttributes))
        .withDelaySeconds(0));
  }
}
