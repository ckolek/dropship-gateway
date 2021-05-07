package me.kolek.ecommerce.dsgw.aws.sns;

import com.amazonaws.DnsResolver;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import java.util.Optional;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.aws.sns.AwsSnsProperties.Endpoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AwsSnsProperties.class)
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class AwsSnsConfiguration {
  private final AwsSnsProperties properties;

  @Bean
  public AmazonSNS sns(AWSCredentialsProvider awsCredentialsProvider,
      Optional<DnsResolver> dnsResolver) {
    var client = AmazonSNSClient.builder()
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
