package me.kolek.ecommerce.dsgw.api.service;

import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.getRequiredAttribute;
import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.toMessageAttributeValueMap;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.context.RequestContext;
import me.kolek.ecommerce.dsgw.internal.model.order.action.OrderAction;
import me.kolek.ecommerce.dsgw.internal.model.queue.MessageAttributes;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor__ = @Inject)
@Slf4j
public class OrderActionService {

  private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  private final Map<String, CompletableFuture<OrderActionResult>> results = new ConcurrentHashMap<>();
  private final ExecutorService resultExecutor = Executors.newCachedThreadPool();

  private String orderActionsQueueUrl;
  private String orderActionResultsQueueUrl;

  private String getOrderActionsQueueUrl() {
    if (orderActionsQueueUrl == null) {
      synchronized (sqs) {
        if (orderActionsQueueUrl == null) {
          orderActionsQueueUrl = sqs.getQueueUrl("order-actions").getQueueUrl();
        }
      }
    }
    return orderActionsQueueUrl;
  }

  private String getOrderActionResultsQueueUrl() {
    if (orderActionResultsQueueUrl == null) {
      synchronized (sqs) {
        if (orderActionResultsQueueUrl == null) {
          orderActionResultsQueueUrl = sqs.createQueue("order-action-results").getQueueUrl();
        }
      }
    }
    return orderActionResultsQueueUrl;
  }

  public OrderActionResult processOrderAction(OrderAction<?> orderAction) throws Exception {
    String body = objectMapper.writeValueAsString(orderAction);

    String requestId = RequestContext.get().getId();
    String responseQueueUrl = getOrderActionResultsQueueUrl();

    Map<String, String> attributes = Map.of(
        MessageAttributes.REQUEST_ID, requestId,
        MessageAttributes.RESPONSE_QUEUE_URL, responseQueueUrl);

    sqs.sendMessage(new SendMessageRequest()
        .withQueueUrl(getOrderActionsQueueUrl())
        .withMessageBody(body)
        .withMessageAttributes(toMessageAttributeValueMap(attributes)));

    CompletableFuture<OrderActionResult> result = new CompletableFuture<>();
    results.put(requestId, result);
    resultExecutor.submit(() -> receiveResultMessages(responseQueueUrl));
    return result.get(10, TimeUnit.SECONDS);
  }

  private Object receiveResultMessages(String queueUrl) throws Exception {
    List<Message> messages;
    do {
      messages = sqs.receiveMessage(new ReceiveMessageRequest()
          .withQueueUrl(queueUrl)
          .withMaxNumberOfMessages(10)
          .withWaitTimeSeconds(10)
          .withAttributeNames("*")).getMessages();

      for (Message message : messages) {
        processOrderActionResultMessage(queueUrl, message);
      }
    } while (!messages.isEmpty());

    return null;
  }

  private void processOrderActionResultMessage(String queueUrl, Message message) throws Exception {
    var result = objectMapper.readValue(message.getBody(), OrderActionResult.class);
    String requestId = getRequiredAttribute(message, MessageAttributes.REQUEST_ID);

    CompletableFuture<OrderActionResult> future = results.remove(requestId);
    if (future != null) {
      future.complete(result);
    } else {
      log.warn("request has expired and result will be ignored: order_id={}", result.getOrderId());
    }

    sqs.deleteMessage(queueUrl, message.getReceiptHandle());
  }
}
