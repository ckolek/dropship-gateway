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

  private final LoadingCache<ServiceLevelKey, Optional<ServiceLevel>> serviceLevelsByCarrierNameAndMode = Caffeine
      .newBuilder().build(key -> getLoader()
          .loadServiceLevelByCarrierNameAndMode(key.getCarrierName(), key.getMode()));

  private final LoadingCache<String, Optional<ServiceLevel>> serviceLevelsByCode = Caffeine
      .newBuilder().build(code -> getLoader().loadServiceLevelByCode(code));

  public Optional<ServiceLevel> findServiceLevelByCarrierNameAndMode(String carrierName,
      String mode) {
    return serviceLevelsByCarrierNameAndMode.get(ServiceLevelKey.of(carrierName, mode));
  }

  public Optional<ServiceLevel> findServiceLevelByCode(String code) {
    return serviceLevelsByCode.get(code);
  }

  @Override
  public void refresh() {
    serviceLevelsByCarrierNameAndMode.invalidateAll();
    serviceLevelsByCode.invalidateAll();
  }

  @Component
  @RequiredArgsConstructor(onConstructor__ = @Inject)
  private static class Loader {

    private final ServiceLevelRepository serviceLevelRepository;

    public Optional<ServiceLevel> loadServiceLevelByCarrierNameAndMode(String carrierName,
        String mode) {
      return serviceLevelRepository.findByCarrierNameAndMode(carrierName, mode);
    }

    public Optional<ServiceLevel> loadServiceLevelByCode(String code) {
      return serviceLevelRepository.findByCode(code);
    }
  }

  @Value(staticConstructor = "of")
  private static class ServiceLevelKey {

    String carrierName;
    String mode;
  }
}
