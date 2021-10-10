package me.kolek.ecommerce.dsgw.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.auth.AuthProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class SecurityConfiguration {
  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private final SecurityProperties properties;
  private final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

  @Bean
  public KeyFactory keyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
    if (properties.getProvider() != null) {
      return KeyFactory.getInstance(properties.getAlgorithm(), properties.getProvider());
    } else {
      return KeyFactory.getInstance(properties.getAlgorithm());
    }
  }

  @Bean
  public PublicKey publicKey(KeyFactory keyFactory) throws IOException, InvalidKeySpecException {
//    byte[] keyBytes = Files.readAllBytes(properties.getPublicKeyPath());
//    OpenSSHPublicKeySpec spec = new OpenSSHPublicKeySpec(keyBytes);
//    return keyFactory.generatePublic(spec);
    return keyPair.getPublic();
  }

  @Bean
  public PrivateKey privateKey(KeyFactory keyFactory) throws IOException, InvalidKeySpecException {
//    byte[] keyBytes = Files.readAllBytes(properties.getPrivateKeyPath());
//    OpenSSHPrivateKeySpec spec = new OpenSSHPrivateKeySpec(keyBytes);
//    return keyFactory.generatePrivate(spec);
    return keyPair.getPrivate();
  }

  private Cipher createCipher()
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    if (properties.getProvider() != null) {
      return Cipher.getInstance(properties.getAlgorithm(), properties.getProvider());
    } else {
      return Cipher.getInstance(properties.getAlgorithm());
    }
  }

  @Bean
  @Encrypt
  public Cipher encryptionCypher(PublicKey publicKey)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
    Cipher cipher = createCipher();
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher;
  }

  @Bean
  @Decrypt
  public Cipher decryptionCypher(PrivateKey privateKey)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
    Cipher cipher = createCipher();
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return cipher;
  }
}
