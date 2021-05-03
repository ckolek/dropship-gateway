package me.kolek.ecommerce.dsgw.registry;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.Optional;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import me.kolek.ecommerce.dsgw.model.ServiceLevel;
import me.kolek.ecommerce.dsgw.repository.ServiceLevelRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class CarrierRegistry implements Registry {

  @Getter(AccessLevel.PRIVATE)
  private final Loader loader;

  private final LoadingCache<ServiceLevelKey, Optional<ServiceLevel>> serviceLevels = Caffeine
      .newBuilder().build(key -> getLoader()
          .loadServiceLevelByCarrierNameAndMode(key.getCarrierName(), key.getMode()));

  public Optional<ServiceLevel> findServiceLevelByCarrierNameAndMode(String carrierName,
      String mode) {
    return serviceLevels.get(ServiceLevelKey.of(carrierName, mode));
  }

  @Override
  public void refresh() {
    serviceLevels.invalidateAll();
  }

  @Component
  @RequiredArgsConstructor(onConstructor__ = @Inject)
  private static class Loader {
    private final ServiceLevelRepository serviceLevelRepository;

    public Optional<ServiceLevel> loadServiceLevelByCarrierNameAndMode(String carrierName,
        String mode) {
      return serviceLevelRepository.findByCarrierNameAndMode(carrierName, mode);
    }
  }

  @Value(staticConstructor = "of")
  private static class ServiceLevelKey {
    String carrierName;
    String mode;
  }
}
