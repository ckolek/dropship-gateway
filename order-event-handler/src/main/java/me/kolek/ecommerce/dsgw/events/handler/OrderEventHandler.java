package me.kolek.ecommerce.dsgw.events.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import me.kolek.ecommerce.dsgw.api.model.event.order.OrderEventDTO;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;

public class OrderEventHandler implements RequestHandler<SNSEvent, String> {
  private final ObjectMapper objectMapper;
  private final RestHighLevelClient elasticsearchClient;

  public OrderEventHandler() {
    this(JacksonUtil.createObjectMapper(), ElasticsearchUtil.createRestHighLevelClient());
  }

  protected OrderEventHandler(ObjectMapper objectMapper, RestHighLevelClient elasticsearchClient) {
    this.objectMapper = objectMapper;
    this.elasticsearchClient = elasticsearchClient;
  }

  @Override
  public String handleRequest(SNSEvent event, Context context) {
    for (var record : event.getRecords()) {
      try {
        handleRecord(record, context);
      } catch (IOException e) {
        LambdaLoggerUtil.printStackTraceMessage(context.getLogger(),
            "failed to handle record: message_id=" + record.getSNS().getMessageId(), e);
        return "500 SERVER ERROR";
      }
    }

    return "200 OK";
  }

  private void handleRecord(SNSRecord record, Context context) throws IOException {
    context.getLogger().log("handling record: message_id=" + record.getSNS().getMessageId() + "\n");

    var orderEvent = parseOrderEvent(record);
    indexOrder(orderEvent, context);
  }

  private OrderEventDTO parseOrderEvent(SNSRecord record) throws JsonProcessingException {
    return objectMapper.readValue(record.getSNS().getMessage(), OrderEventDTO.class);
  }

  private void indexOrder(OrderEventDTO orderEvent, Context context) throws IOException {
    var logger = context.getLogger();

    var metadata = orderEvent.getMetadata();
    var order = orderEvent.getOrder();

    logger.log("indexing order from event: rid=" + metadata.getRequestId() + " order_id=" + metadata
        .getOrderId() + " order_version=" + metadata.getOrderVersion() + " time_emitted=" + metadata
        .getTimeEmitted() + "\n");

    byte[] source = objectMapper.writeValueAsBytes(order);

    IndexRequest indexRequest = new IndexRequest()
        .index("orders")
        .id(order.getId())
        .source(source, XContentType.JSON)
        .version(metadata.getOrderVersion())
        .versionType(VersionType.EXTERNAL);

    var response = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);

    logger.log("received index response: " + response);
  }
}