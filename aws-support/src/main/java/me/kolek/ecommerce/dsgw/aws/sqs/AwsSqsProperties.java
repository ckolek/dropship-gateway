package me.kolek.ecommerce.dsgw.aws.sqs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.DnsResolver;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import java.time.Duration;
import java.util.Optional;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws.sqs")
@Data
public class AwsSqsProperties {

  private String region;
  private Client client;
  private Endpoint endpoint;

  @Data
  public static final class Client {

    private Boolean cacheResponseMetadata;
    private Duration clientExecutionTimeout;
    private Duration connectionMaxIdle;
    private Duration connectionTtl;
    private Duration connectionTimeout;
    private Boolean disableHostPrefixInjection;
    private Boolean disableSocketProxy;
    private Boolean gzip;
    private Duration socketTimeout;

    public ClientConfiguration toConfiguration(Optional<DnsResolver> dnsResolver) {
      var configuration = new ClientConfiguration();
      Optional.ofNullable(cacheResponseMetadata)
          .ifPresent(configuration::withCacheResponseMetadata);
      Optional.ofNullable(clientExecutionTimeout).map(Duration::toMillis)
          .ifPresent(millis -> configuration.withClientExecutionTimeout(millis.intValue()));
      Optional.ofNullable(connectionTimeout).map(Duration::toMillis)
          .ifPresent(millis -> configuration.withConnectionTimeout(millis.intValue()));
      dnsResolver.ifPresent(configuration::withDnsResolver);
      Optional.ofNullable(socketTimeout).map(Duration::toMillis)
          .ifPresent(millis -> configuration.withSocketTimeout(millis.intValue()));
      return configuration;
    }
  }

  @Data
  public static final class Endpoint {

    private String serviceEndpoint;
    private String signingRegion;

    public EndpointConfiguration toConfiguration() {
      return new EndpointConfiguration(serviceEndpoint, signingRegion);
    }
  }
}
