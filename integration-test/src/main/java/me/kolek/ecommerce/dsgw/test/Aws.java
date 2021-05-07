package me.kolek.ecommerce.dsgw.test;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Aws {

  private static final String AWS_ENDPOINT = Optional.ofNullable(System.getenv("AWS_ENDPOINT"))
      .orElse("http://localhost:4566");
  private static final String AWS_REGION = Optional.ofNullable(System.getenv("AWS_REGION"))
      .orElse("us-east-1");

  private final AmazonSQS sqs = AmazonSQSClient.builder()
      .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
      .withEndpointConfiguration(new EndpointConfiguration(AWS_ENDPOINT, AWS_REGION))
      .build();

  private final Map<String, String> queueNameToUrl = new ConcurrentHashMap<>();

  private String getQueueUrl(String queueName) {
    return queueNameToUrl.computeIfAbsent(queueName, qn -> sqs.getQueueUrl(qn).getQueueUrl());
  }

  private List<Message> receiveMessages(String queueUrl, Duration waitTime) {
    return sqs.receiveMessage(new ReceiveMessageRequest()
        .withQueueUrl(queueUrl)
        .withAttributeNames("All")
        .withMessageAttributeNames("All")
        .withMaxNumberOfMessages(10)
        .withWaitTimeSeconds((int) waitTime.toSeconds()))
        .getMessages();
  }

  private void deleteMessages(String queueUrl, List<Message> messages) {
    messages.forEach(message -> sqs.deleteMessage(queueUrl, message.getReceiptHandle()));
  }

  public void processMessages(String queueName, Duration waitTime,
      Consumer<Message> messageConsumer) {
    String queueUrl = getQueueUrl(queueName);

    List<Message> messages;
    do {
      messages = receiveMessages(queueUrl, waitTime);
      messages.forEach(messageConsumer);
      deleteMessages(queueUrl, messages);
    } while (!messages.isEmpty());
  }

  public void purgeQueue(String queueName) {
    sqs.purgeQueue(new PurgeQueueRequest().withQueueUrl(getQueueUrl(queueName)));
  }
}
