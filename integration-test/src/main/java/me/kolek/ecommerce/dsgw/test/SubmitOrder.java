package me.kolek.ecommerce.dsgw.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderItem;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRecipient;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.test.api.GraphQLInvoker;

@RequiredArgsConstructor
public class SubmitOrder {

  private final GraphQLInvoker graphQLInvoker;

  private OrderDTO submittedOrder;

  @When("An order is submitted")
  public void submitOrder() throws Exception {
    var request = SubmitOrderRequest.builder()
        .orderNumber("1000001-1")
        .warehouseCode("CWK1")
        .recipient(SubmitOrderRecipient.builder()
            .name("Chris Kolek")
            .email("ckolek@gmail.com")
            .phone("978-846-4525")
            .line1("100 Heard Street")
            .line2("Unit 314")
            .city("Chelsea")
            .state("MA")
            .postalCode("02150")
            .country("USA")
            .build())
        .item(SubmitOrderItem.builder()
            .sku("PN1001")
            .quantity(10)
            .build())
        .build();

    submittedOrder = graphQLInvoker
        .invoke("SubmitOrder", Map.of("request", request), OrderDTO.class);
  }

  @Then("An order with a new ID is returned")
  public void orderWithIdWasReturned() {
    assertThat(submittedOrder).isNotNull();
    assertThat(submittedOrder.getId()).isNotBlank();
  }
}
