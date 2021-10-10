package me.kolek.ecommerce.dsgw.events.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import me.kolek.ecommerce.dsgw.api.model.event.OrderEventDTO;
import me.kolek.ecommerce.dsgw.events.handler.util.ElasticsearchUtil;
import me.kolek.ecommerce.dsgw.events.handler.util.JacksonUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;

public class OrderEventHandler extends SNSEventRequestHandler<OrderEventDTO> {
  private final RestHighLevelClient elasticsearchClient;

  public OrderEventHandler() {
    this(JacksonUtil.createObjectMapper(), ElasticsearchUtil.createRestHighLevelClient());
  }

  protected OrderEventHandler(ObjectMapper objectMapper, RestHighLevelClient elasticsearchClient) {
    super(objectMapper, OrderEventDTO.class);
    this.elasticsearchClient = elasticsearchClient;
  }

  @Override
  protected void handleRecord(OrderEventDTO orderEvent, Context context) throws IOException {
    var logger = context.getLogger();

    var metadata = orderEvent.getMetadata();
    var order = orderEvent.getRecord();

    logger.log("indexing order from event: rid=" + metadata.getRequestId() + " order_id=" + metadata
        .getRecordId() + " order_version=" + metadata.getRecordVersion() + " time_emitted=" + metadata
        .getTimeEmitted() + "\n");

    byte[] source = objectMapper.writeValueAsBytes(order);

    IndexRequest indexRequest = new IndexRequest()
        .index("orders")
        .id(order.getId())
        .source(source, XContentType.JSON)
        .version(metadata.getRecordVersion())
        .versionType(VersionType.EXTERNAL);

    var response = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);

    logger.log("received index response: " + response);
  }
}