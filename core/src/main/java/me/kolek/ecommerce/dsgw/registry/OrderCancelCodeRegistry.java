package me.kolek.ecommerce.dsgw.registry;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.Optional;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.model.OrderCancelCode;
import me.kolek.ecommerce.dsgw.repository.OrderCancelCodeRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class OrderCancelCodeRegistry implements Registry {

  @Getter(AccessLevel.PRIVATE)
  private final Loader loader;

  private final LoadingCache<String, Optional<OrderCancelCode>> orderCancelCodesByCode = Caffeine
      .newBuilder()
      .build(code -> getLoader().loadOrderCancelCodeByCode(code));

  public Optional<OrderCancelCode> findOrderCancelCodeByCode(String code) {
    return orderCancelCodesByCode.get(code);
  }

  @Override
  public void refresh() {
    orderCancelCodesByCode.invalidateAll();
  }

  @Component
  @RequiredArgsConstructor(onConstructor__ = @Inject)
  private static class Loader {
    private final OrderCancelCodeRepository orderCancelCodeRepository;

    public Optional<OrderCancelCode> loadOrderCancelCodeByCode(String code) {
      return orderCancelCodeRepository.findByCode(code);
    }
  }
}
