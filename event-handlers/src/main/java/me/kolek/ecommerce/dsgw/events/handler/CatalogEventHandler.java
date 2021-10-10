package me.kolek.ecommerce.dsgw.events.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import me.kolek.ecommerce.dsgw.api.model.event.CatalogEventDTO;
import me.kolek.ecommerce.dsgw.events.handler.util.ElasticsearchUtil;
import me.kolek.ecommerce.dsgw.events.handler.util.JacksonUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;

public class CatalogEventHandler extends SNSEventRequestHandler<CatalogEventDTO> {
  private final RestHighLevelClient elasticsearchClient;

  public CatalogEventHandler() {
    this(JacksonUtil.createObjectMapper(), ElasticsearchUtil.createRestHighLevelClient());
  }

  protected CatalogEventHandler(ObjectMapper objectMapper, RestHighLevelClient elasticsearchClient) {
    super(objectMapper, CatalogEventDTO.class);
    this.elasticsearchClient = elasticsearchClient;
  }

  @Override
  protected void handleRecord(CatalogEventDTO catalogEvent, Context context) throws IOException {
    var logger = context.getLogger();

    var metadata = catalogEvent.getMetadata();
    var catalog = catalogEvent.getRecord();

    logger.log("indexing catalog from event: rid=" + metadata.getRequestId() + " order_id=" + metadata
        .getRecordId() + " order_version=" + metadata.getRecordVersion() + " time_emitted=" + metadata
        .getTimeEmitted() + "\n");

    byte[] source = objectMapper.writeValueAsBytes(catalog);

    IndexRequest indexRequest = new IndexRequest()
        .index("catalogs")
        .id(catalog.getId())
        .source(source, XContentType.JSON)
        .version(metadata.getRecordVersion())
        .versionType(VersionType.EXTERNAL);

    var response = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);

    logger.log("received index response: " + response);
  }
}