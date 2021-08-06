package me.kolek.ecommerce.dsgw.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.kolek.ecommerce.dsgw.registry.CarrierRegistry;
import me.kolek.ecommerce.dsgw.registry.OrderCancelCodeRegistry;
import me.kolek.ecommerce.dsgw.repository.OrderCancelCodeRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class CommonStubbing {

  public static void execute(TransactionTemplate transactionTemplate,
      TransactionStatus transactionStatus) {
    when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
      TransactionCallback<?> callback = invocation.getArgument(0);
      return callback.doInTransaction(transactionStatus);
    });
  }

  public static TransactionStatus execute(TransactionTemplate transactionTemplate) {
    TransactionStatus transactionStatus = mock(TransactionStatus.class);
    execute(transactionTemplate, transactionStatus);
    return transactionStatus;
  }

  public static void resolveStandardServiceLevels(CarrierRegistry carrierRegistry) {
    when(carrierRegistry.findServiceLevelByCarrierNameAndMode(any(), any()))
        .thenReturn(Optional.empty());
    when(carrierRegistry.findServiceLevelByCode(any()))
        .thenReturn(Optional.empty());

    for (var serviceLevel : ReferenceData.ServiceLevels.values()) {
      when(carrierRegistry.findServiceLevelByCarrierNameAndMode(serviceLevel.getCarrier().getName(),
          serviceLevel.getMode())).thenReturn(Optional.of(serviceLevel));
      when(carrierRegistry.findServiceLevelByCode(serviceLevel.getCode()))
          .thenReturn(Optional.of(serviceLevel));
    }
  }

  public static void resolveStandardOrderCancelCodes(
      OrderCancelCodeRegistry orderCancelCodeRegistry) {
    when(orderCancelCodeRegistry.findOrderCancelCodeByCode(any())).thenReturn(Optional.empty());

    for (var orderCancelCode : ReferenceData.OrderCancelCodes.values()) {
      when(orderCancelCodeRegistry.findOrderCancelCodeByCode(orderCancelCode.getCode()))
          .thenReturn(Optional.of(orderCancelCode));
    }
  }

  public static <T, ID> void find(CrudRepository<T, ID> repository, T entity,
      Function<T, ID> idGetter) {
    when(repository.findById(idGetter.apply(entity))).thenReturn(Optional.of(entity));
  }

  public static <T, ID> void save(CrudRepository<T, ID> repository, Supplier<ID> idGenerator,
      BiConsumer<T, ID> idSetter) {
    save(repository, entity -> idGenerator.get(), idSetter);
  }

  public static <T, ID> void save(CrudRepository<T, ID> repository, Function<T, ID> idGenerator,
      BiConsumer<T, ID> idSetter) {
    when(repository.save(any())).thenAnswer(invocation -> {
      T entity = invocation.getArgument(0);
      idSetter.accept(entity, idGenerator.apply(entity));
      return entity;
    });
  }
}
