package me.kolek.ecommerce.dsgw.registry;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.model.Warehouse;
import me.kolek.ecommerce.dsgw.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class WarehouseRegistry implements Registry {

  @Getter(AccessLevel.PRIVATE)
  private final Loader loader;

  private final LoadingCache<String, Optional<Warehouse>> warehousesByCode = Caffeine.newBuilder()
      .build(code -> getLoader().loadWarehouseByCode(code));

  public Optional<Warehouse> findWarehouseByCode(String code) {
    return warehousesByCode.get(code);
  }

  @Override
  public void refresh() {
    warehousesByCode.invalidateAll();
  }

  @Component
  @RequiredArgsConstructor(onConstructor__ = @Inject)
  private static class Loader {

    private final WarehouseRepository warehouseRepository;

    @Transactional
    public Optional<Warehouse> loadWarehouseByCode(String code) {
      return warehouseRepository.findWarehouseByCode(code);
    }
  }
}
