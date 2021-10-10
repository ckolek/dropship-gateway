package me.kolek.ecommerce.dsgw.api.http;

import java.util.List;
import me.kolek.ecommerce.dsgw.auth.AuthProperties;
import me.kolek.ecommerce.dsgw.auth.token.AuthTokenGenerator;
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
    registration.setOrder(-2);
    registration.setUrlPatterns(List.of("*"));
    return registration;
  }

  @Bean
  public FilterRegistrationBean<AuthFilter> authFilterFilterRegistration(
      AuthProperties authProperties, AuthTokenGenerator tokenGenerator) {
    var registration = new FilterRegistrationBean<AuthFilter>();
    registration.setFilter(new AuthFilter(authProperties, tokenGenerator));
    registration.setEnabled(authProperties.isEnabled());
    registration.setOrder(-1);
    registration.setUrlPatterns(authProperties.getUrlPatterns());
    return registration;
  }
}
