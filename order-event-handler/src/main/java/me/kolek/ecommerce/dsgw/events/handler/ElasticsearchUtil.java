package me.kolek.ecommerce.dsgw.events.handler;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElasticsearchUtil {
  private static final String ELASTICSEARCH_HOSTS = "ELASTICSEARCH_HOSTS";
  private static final Splitter HOSTS_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

  public static RestHighLevelClient createRestHighLevelClient() {
    return createRestHighLevelClient(getElasticsearchHosts());
  }

  public static RestHighLevelClient createRestHighLevelClient(HttpHost[] httpHosts) {
    return new RestHighLevelClient(RestClient.builder(httpHosts));
  }

  public static HttpHost[] getElasticsearchHosts() {
    String elasticsearchHostsValue = System.getenv(ELASTICSEARCH_HOSTS);

    return Streams.stream(HOSTS_SPLITTER.split(elasticsearchHostsValue))
        .map(HttpHost::create)
        .toArray(HttpHost[]::new);
  }
}
