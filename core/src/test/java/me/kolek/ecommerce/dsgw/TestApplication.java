package me.kolek.ecommerce.dsgw;

import me.kolek.ecommerce.dsgw.repository.impl.ExtendedJpaRepositoryImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EntityScan(basePackages = "me.kolek.ecommerce.dsgw.model")
@EnableJpaRepositories(basePackages = "me.kolek.ecommerce.dsgw.repository",
    repositoryBaseClass = ExtendedJpaRepositoryImpl.class)
public class TestApplication {

}
