package me.kolek.ecommerce.dsgw.aws.sqs;

import com.amazonaws.DnsResolver;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import java.util.Optional;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.aws.sqs.AwsSqsProperties.Endpoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AwsSqsProperties.class)
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class AwsSqsConfiguration {
  private final AwsSqsProperties properties;

  @Bean
  public AmazonSQS sqs(AWSCredentialsProvider awsCredentialsProvider,
      Optional<DnsResolver> dnsResolver) {
    var client = AmazonSQSClient.builder()
        .withCredentials(awsCredentialsProvider);
    Optional.ofNullable(properties.getEndpoint())
        .map(Endpoint::toConfiguration)
        .ifPresentOrElse(client::withEndpointConfiguration,
            () -> Optional.ofNullable(properties.getRegion()).ifPresent(client::withRegion));
    Optional.ofNullable(properties.getClient())
        .map(c -> c.toConfiguration(dnsResolver))
        .ifPresent(client::withClientConfiguration);
    return client.build();
  }
}
