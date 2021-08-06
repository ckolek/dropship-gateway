package me.kolek.ecommerce.dsgw.worker.processor;

import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.acceptLineExplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.acceptLineImplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.rejectLineExplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.rejectLineImplicit;
import static me.kolek.ecommerce.dsgw.test.AcknowledgeOrder.rejectLinePartial;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.execute;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.find;
import static me.kolek.ecommerce.dsgw.test.CommonStubbing.resolveStandardOrderCancelCodes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.temporal.ChronoUnit;
import me.kolek.ecommerce.dsgw.api.model.action.order.OrderActionResult.Status;
import me.kolek.ecommerce.dsgw.api.model.action.order.acknowledge.AcknowledgeOrderRequest;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentRequest;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO.Type;
import me.kolek.ecommerce.dsgw.events.OrderEventEmitter;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AcknowledgeOrderAction;
import me.kolek.ecommerce.dsgw.internal.model.order.action.AddOrderShipmentAction;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.registry.CarrierRegistry;
import me.kolek.ecommerce.dsgw.registry.WarehouseRegistry;
import me.kolek.ecommerce.dsgw.repository.OrderRepository;
import me.kolek.ecommerce.dsgw.repository.PackageRepository;
import me.kolek.ecommerce.dsgw.test.Mappers;
import me.kolek.ecommerce.dsgw.test.Orders;
import me.kolek.ecommerce.dsgw.test.ReferenceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddOrderShipmentActionProcessorTest {
  @Mock
  private OrderRepository orderRepository;

  @Mock
  private PackageRepository packageRepository;

  @Mock
  private WarehouseRegistry warehouseRegistry;

  @Mock
  private CarrierRegistry carrierRegistry;

  @Mock
  private OrderEventEmitter orderEventEmitter;

  @Mock
  private TransactionTemplate transactionTemplate;

  @Mock
  private TransactionStatus transactionStatus;

  private AddOrderShipmentActionProcessor processor;

  @BeforeEach
  public void setUp() {
    processor = new AddOrderShipmentActionProcessor(orderRepository, Mappers._package(),
        Mappers.packageItem(), packageRepository, warehouseRegistry, carrierRegistry,
        orderEventEmitter);
    processor.setTransactionTemplate(transactionTemplate);

    execute(transactionTemplate, transactionStatus);
  }

  @Test
  public void testSuccessfulShippedPartial() throws Exception {
    var order = Orders.newOrder(6);
    var orderDto = Mappers.order().orderToDto(order);

  }

  @Test
  public void testSuccessfulShippedComplete() throws Exception {
    var order = Orders.newOrder(6);
    var orderDto = Mappers.order().orderToDto(order);

  }

  @Test
  public void testFailedOrderNotFound() {
    var request = OrderShipmentRequest.builder()
        .build();

    var action = AddOrderShipmentAction.builder()
        .orderId("100001")
        .request(request)
        .build();

    var result = processor.process(action);
    assertThat(result.getReasons()).describedAs("result reasons").satisfies(reasons -> {
      assertThat(reasons).anySatisfy(reason -> {
        assertThat(reason.getDescription()).describedAs("description")
            .contains(action.getOrderId())
            .contains("not found");
      });
    });
    assertThat(result.getStatus()).describedAs("result status").isEqualTo(Status.FAILED);
    assertThat(result.getOrderId()).describedAs("result order ID").isNull();

    verify(orderRepository, never()).save(any());
    verify(transactionStatus).setRollbackOnly();
    verifyNoInteractions(orderEventEmitter);
  }
}
