package me.kolek.ecommerce.dsgw.events.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Events;
import com.amazonaws.services.lambda.runtime.tests.annotations.HandlerParams;
import com.amazonaws.services.lambda.runtime.tests.annotations.Responses;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.events.handler.util.ElasticsearchUtil;
import me.kolek.ecommerce.dsgw.events.handler.util.JacksonUtil;
import org.apache.http.HttpHost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(MockitoExtension.class)
@Testcontainers
@Slf4j
public class OrderEventHandlerTest {

  @Container
  private static final ElasticsearchContainer ELASTICSEARCH = new ElasticsearchContainer(
      DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch").withTag("7.13.3"));

  @Mock
  private Context context;

  private OrderEventHandler handler;

  @BeforeEach
  @SneakyThrows
  public void setUp() {
    var objectMapper = JacksonUtil.createObjectMapper();
    var elasticsearchClient = ElasticsearchUtil.createRestHighLevelClient(new HttpHost[] {
        new HttpHost(ELASTICSEARCH.getHttpHostAddress())
    });

    when(context.getLogger()).thenReturn(new LambdaLogger() {
      @Override
      public void log(String message) {
        log.info(message);
      }

      @Override
      public void log(byte[] message) {
        log.info(new String(message));
      }
    });

    handler = new OrderEventHandler(objectMapper, elasticsearchClient);
  }

  @ParameterizedTest
  @HandlerParams(
      events = @Events(
          folder = "/me/kolek/ecommerce/dsgw/events/handler/order/requests",
          type = SNSEvent.class),
      responses = @Responses(
          folder = "/me/kolek/ecommerce/dsgw/events/handler/order/responses",
          type = String.class))
  public void testHandleRequest(SNSEvent event, String expectedResponse) {
    String actualResponse = handler.handleRequest(event, context);

    assertThat(actualResponse).describedAs("response").isEqualTo(expectedResponse);
  }
}
