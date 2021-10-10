package me.kolek.ecommerce.dsgw.auth;

import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class AuthConfiguration {

}
