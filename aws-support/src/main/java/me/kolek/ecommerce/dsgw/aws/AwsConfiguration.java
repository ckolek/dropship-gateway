package me.kolek.ecommerce.dsgw.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {
  @Bean
  public AWSCredentialsProvider awsCredentialsProvider() {
    return DefaultAWSCredentialsProviderChain.getInstance();
  }
}
