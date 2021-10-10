package me.kolek.ecommerce.dsgw.auth;

import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.auth.AuthException.Type;
import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import me.kolek.ecommerce.dsgw.model.ClientCredentials.ClientType;
import me.kolek.ecommerce.dsgw.repository.ClientCredentialsRepository;
import me.kolek.ecommerce.dsgw.security.Encrypt;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CredentialManager {
  private final ClientCredentialsRepository clientCredentialsRepository;
  private final Cipher encryptionCipher;

  @Inject
  public CredentialManager(ClientCredentialsRepository clientCredentialsRepository,
      @Encrypt Cipher encryptionCipher) {
    this.clientCredentialsRepository = clientCredentialsRepository;
    this.encryptionCipher = encryptionCipher;
  }

  public ClientCredentials provisionCredentials(String clientId, String clientSecret,
      ClientType clientType) {
    ClientCredentials credentials = ClientCredentials.builder().clientId(clientId)
        .clientId(clientId)
        .clientSecret(encryptSecret(clientSecret))
        .clientType(clientType)
        .build();

    return clientCredentialsRepository.save(credentials);
  }

  @Transactional
  public ClientCredentials resetCredentials(String clientId, String clientSecret) {
    ClientCredentials credentials = clientCredentialsRepository.findByClientId(clientId)
        .orElseThrow(() -> new AuthException("invalid client credentials", Type.UNAUTHORIZED));
    credentials.setClientSecret(encryptSecret(clientSecret));

    return clientCredentialsRepository.save(credentials);
  }

  public ClientCredentials validateCredentials(String clientId, String clientSecret) {
    return clientCredentialsRepository.findByClientId(clientId)
        .filter(c -> secretMatches(c, clientSecret))
        .orElseThrow(() -> new AuthException("invalid client credentials", Type.UNAUTHORIZED));
  }

  private boolean secretMatches(ClientCredentials credentials, String clientSecret) {
    return Arrays.equals(credentials.getClientSecret(), encryptSecret(clientSecret));
  }

  private byte[] encryptSecret(String clientSecret) {
    byte[] encryptedSecret;
    try {
      encryptedSecret = encryptionCipher.doFinal(clientSecret.getBytes());
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      log.error("failed to process client secret", e);
      throw new AuthException("failed to process client secret", Type.UNAUTHORIZED);
    }
    return Base64.getEncoder().encode(encryptedSecret);
  }
}
