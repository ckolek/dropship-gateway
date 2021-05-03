package me.kolek.ecommerce.dsgw.aws.sqs;

import java.time.Duration;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws.sqs.processing")
@Data
public class QueueProcessingProperties {
  private Map<String, QueueConfig> queues;

  @Data
  public static class QueueConfig {
    private String queueName;
    private Duration pollInterval = Duration.ofSeconds(30);
    private Integer maxNumberOfMessages;
    private Duration waitTime = Duration.ofSeconds(10);
    private String[] attributeNames = new String[0];
    private String[] messageAttributeNames = new String[0];
    private WorkerPoolConfig workerPool = new WorkerPoolConfig();
    private String backOffStrategy;

    @Data
    public static class WorkerPoolConfig {
      private int threadCount = 10;
    }
  }
}
