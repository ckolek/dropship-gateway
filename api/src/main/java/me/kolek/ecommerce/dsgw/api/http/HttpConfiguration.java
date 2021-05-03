package me.kolek.ecommerce.dsgw.api.http;

import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfiguration {

  @Bean
  public FilterRegistrationBean<RequestContextInitializingFilter> requestContextFilterRegistration() {
    var registration = new FilterRegistrationBean<RequestContextInitializingFilter>();
    registration.setFilter(new RequestContextInitializingFilter());
    registration.setEnabled(true);
    registration.setOrder(-1);
    registration.setUrlPatterns(List.of("*"));
    return registration;
  }
}
