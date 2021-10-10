package me.kolek.ecommerce.dsgw.events.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("dsgw.events")
@Data
public class EventProperties {
  private String catalogEventTopicArn;
  private String orderEventTopicArn;
}
