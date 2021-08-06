package me.kolek.ecommerce.dsgw.search.impl;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

@Configuration
public class ElasticsearchConfiguration {

  @Bean
  public ElasticsearchCustomConversions elasticsearchCustomConversions() {
    return new ElasticsearchCustomConversions(List.of(new StringToOffsetDateTime()));
  }

  @ReadingConverter
  static class StringToOffsetDateTime implements Converter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(String source) {
      return OffsetDateTime.parse(source);
    }
  }
}
