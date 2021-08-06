package me.kolek.ecommerce.dsgw.api.service;

import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.getRequiredAttribute;
import static me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsUtil.toMessageAttributeValueMap;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
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

  private final Supplier<String> orderActionsQueueUrl = Suppliers
      .synchronizedSupplier(() -> getQueueUrl("order-actions"));
  private final Supplier<String> orderActionResultsQueueUrl = Suppliers
      .synchronizedSupplier(() -> getQueueUrl("order-action-results"));

  public OrderActionResult processOrderAction(OrderAction<?> orderAction, boolean async)
      throws Exception {
    String body = objectMapper.writeValueAsString(orderAction);

    String requestId = RequestContext.get().getId();
    String responseQueueUrl = orderActionResultsQueueUrl.get();

    Map<String, String> attributes = new HashMap<>();
    attributes.put(MessageAttributes.REQUEST_ID, requestId);

    CompletableFuture<OrderActionResult> result = new CompletableFuture<>();

    if (!async) {
      attributes.put(MessageAttributes.RESPONSE_QUEUE_URL, responseQueueUrl);
      results.put(requestId, result);
    }

    var sendMessageResponse = sqs.sendMessage(new SendMessageRequest()
        .withQueueUrl(orderActionsQueueUrl.get())
        .withMessageBody(body)
        .withMessageAttributes(toMessageAttributeValueMap(attributes)));

    log.debug("sent order action message: message_id={}", sendMessageResponse.getMessageId());

    if (!async) {
      resultExecutor.submit(() -> {
        try {
          receiveResultMessages(responseQueueUrl);
        } catch (Exception e) {
          log.error("failed to receive order action result messages", e);
        }
      });

      try {
        return result.get(10, TimeUnit.SECONDS);
      } finally {
        results.remove(requestId);
      }
    } else {
      return OrderActionResult.builder().status(Status.PENDING).build();
    }
  }

  private Object receiveResultMessages(String queueUrl) throws Exception {
    List<Message> messages;
    do {
      messages = sqs.receiveMessage(new ReceiveMessageRequest()
          .withQueueUrl(queueUrl)
          .withMaxNumberOfMessages(10)
          .withWaitTimeSeconds(10)
          .withMessageAttributeNames("All"))
          .getMessages();

      for (Message message : messages) {
        processOrderActionResultMessage(queueUrl, message);
      }
    } while (!messages.isEmpty());

    return null;
  }

  private void processOrderActionResultMessage(String queueUrl, Message message) throws Exception {
    String requestId = getRequiredAttribute(message, MessageAttributes.REQUEST_ID);

    try (var requestContext = RequestContext.initialize(requestId)) {
      log.debug("processing order action result message: message_id={}", message.getMessageId());

      var result = objectMapper.readValue(message.getBody(), OrderActionResult.class);

      CompletableFuture<OrderActionResult> future = results.remove(requestId);
      if (future != null) {
        future.complete(result);
      } else {
        log.warn("request has expired and result will be ignored: order_id={}",
            result.getOrderId());
      }

      sqs.deleteMessage(queueUrl, message.getReceiptHandle());
    }
  }

  private String getQueueUrl(String queueName) {
    return sqs.getQueueUrl(queueName).getQueueUrl();
  }
}
