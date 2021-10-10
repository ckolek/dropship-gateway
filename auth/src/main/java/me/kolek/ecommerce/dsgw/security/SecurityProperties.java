package me.kolek.ecommerce.dsgw.security;

import java.beans.ConstructorProperties;
import java.nio.file.Path;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("dsgw.security")
public class SecurityProperties {
  private String algorithm = "RSA";
  private String provider = "BC";
  private Path publicKeyPath;
  private Path privateKeyPath;
}
