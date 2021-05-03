package me.kolek.ecommerce.dsgw.aws.sqs;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequest;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequestEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageSystemAttributeName;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.aws.sqs.QueueProcessingProperties.QueueConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(QueueProcessingProperties.class)
@RequiredArgsConstructor(onConstructor__ = @Inject)
@Slf4j
public class QueuePoller {
  private final AmazonSQS sqs;
  private final Collection<MessageProcessor> messageProcessors;
  private final QueueProcessingProperties properties;

  private ScheduledExecutorService executorService;

  @PostConstruct
  public void start() {
    if (executorService == null) {
      List<QueueWrapper> wrappers = new ArrayList<>(messageProcessors.size());
      for (MessageProcessor messageProcessor : messageProcessors) {
        String queueLabel = messageProcessor.getQueueLabel();

        var queueConfig = properties.getQueues().get(queueLabel);
        if (queueConfig != null) {
          wrappers.add(new QueueWrapper(queueLabel, queueConfig, messageProcessor));
        } else {
          log.warn("no queue processing configuration found: queue_label={}", queueLabel);
        }
      }

      executorService = Executors.newScheduledThreadPool(wrappers.size());

      wrappers.forEach(wrapper -> wrapper.schedule(executorService));
    }
  }

  @PreDestroy
  private void shutdown() {
    if (executorService != null) {
      executorService.shutdownNow();
    }
  }

  @RequiredArgsConstructor
  private class QueueWrapper implements Runnable {
    @Getter
    private final String label;
    @Getter
    private final QueueConfig config;

    private final MessageProcessor messageProcessor;
    private final WorkerPool workerPool;
    private final BackOffStrategy backOffStrategy;

    private String queueUrl;

    public QueueWrapper(String label, QueueConfig config, MessageProcessor messageProcessor) {
      this.label = label;
      this.config = config;
      this.messageProcessor = messageProcessor;
      this.workerPool = new WorkerPool(config.getWorkerPool().getThreadCount());
      this.backOffStrategy = BackOffStrategy.valueOf(config.getBackOffStrategy()).orElse(null);
    }

    private void schedule(ScheduledExecutorService executorService) {
      executorService
          .scheduleAtFixedRate(this, 0, config.getPollInterval().toSeconds(), TimeUnit.SECONDS);
    }

    private String getQueueUrl() {
      if (queueUrl == null) {
        queueUrl = sqs.getQueueUrl(config.getQueueName()).getQueueUrl();
      }
      return queueUrl;
    }

    private ReceiveMessageRequest createReceiveMessageRequest() {
      return new ReceiveMessageRequest()
          .withQueueUrl(getQueueUrl())
          .withMaxNumberOfMessages(config.getMaxNumberOfMessages())
          .withWaitTimeSeconds((int) config.getWaitTime().toSeconds())
          .withAttributeNames(config.getAttributeNames())
          .withMessageAttributeNames(config.getMessageAttributeNames());
    }

    @Override
    public void run() {
      List<Message> messages;
      try {
        messages = sqs.receiveMessage(createReceiveMessageRequest()).getMessages();
      } catch (SdkBaseException e) {
        log.error("failed to poll queue for messages", e);
        return;
      }

      log.info("received {} messages", messages.size());

      var rejectedMessages = new ArrayList<Message>(messages.size());

      for (Message message : messages) {
        if (!workerPool.submit(message)) {
          rejectedMessages.add(message);
        }
      }

      changeMessageVisibilityBatch(rejectedMessages);
    }

    private OptionalInt calculateBackOffVisibilityTimeout(Message message) {
      if (backOffStrategy != null) {
        return OptionalInt
            .of(backOffStrategy.calculateVisibilityTimeout(getReceiveCount(message)));
      }
      return OptionalInt.empty();
    }

    private ChangeMessageVisibilityBatchRequest createChangeMessageVisibilityBatchRequest(List<Message> messages) {
      return new ChangeMessageVisibilityBatchRequest()
          .withQueueUrl(getQueueUrl())
          .withEntries(messages.stream()
              .map(this::createChangeMessageVisibilityBatchRequest)
              .collect(Collectors.toList()));
    }

    private ChangeMessageVisibilityBatchRequestEntry createChangeMessageVisibilityBatchRequest(
        Message message) {
      return new ChangeMessageVisibilityBatchRequestEntry()
          .withId(message.getMessageId())
          .withReceiptHandle(message.getReceiptHandle())
          .withVisibilityTimeout(calculateBackOffVisibilityTimeout(message)
              .orElse(0));
    }

    private void changeMessageVisibilityBatch(List<Message> messages) {
      try {
        sqs.changeMessageVisibilityBatch(createChangeMessageVisibilityBatchRequest(messages));
      } catch (SdkBaseException e) {
        log.error("failed to change message batch visibility", e);
      }
    }

    private class WorkerPool {
      private final ExecutorService executor;

      public WorkerPool(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
      }

      public boolean submit(Message message) {
        try {
          executor.submit(new MessageProcessing(message));
          return true;
        } catch (RejectedExecutionException e) {
          return false;
        }
      }

      @RequiredArgsConstructor
      private class MessageProcessing implements Runnable {
        private final Message message;

        @Override
        public void run() {
          try {
            var result = messageProcessor.processMessage(message);
            if (result.isRetry()) {
              backOffMessageVisibility();
            } else {
              deleteMessage();
            }
          } catch (Exception e) {
            log.error("failed to process SQS message: message_id={}", message.getMessageId(), e);
            backOffMessageVisibility();
          }
        }

        private void deleteMessage() {
          try {
            sqs.deleteMessage(getQueueUrl(), message.getReceiptHandle());
          } catch (SdkBaseException e) {
            log.error("failed to delete message: message_id={}, receipt_handle={}",
                message.getMessageId(), message.getReceiptHandle(), e);
          }
        }

        private void backOffMessageVisibility() {
          calculateBackOffVisibilityTimeout(message).ifPresent(this::changeMessageVisibility);
        }

        private void changeMessageVisibility(int visibilityTimeout) {
          try {
            sqs.changeMessageVisibility(getQueueUrl(), message.getReceiptHandle(),
                visibilityTimeout);
          } catch (SdkBaseException e) {
            log.error("failed to change message visibility: message_id={}, receipt_handle={}",
                message.getMessageId(), message.getReceiptHandle(), e);
          }
        }
      }
    }
  }

  private static int getReceiveCount(Message message) {
    return Optional.ofNullable(message.getAttributes()
        .get(MessageSystemAttributeName.ApproximateReceiveCount.name()))
        .map(Integer::valueOf)
        .orElse(1);
  }
}
