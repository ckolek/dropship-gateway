package me.kolek.ecommerce.dsgw.api.graphql;

import graphql.kickstart.tools.SchemaParserDictionary;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import me.kolek.ecommerce.dsgw.api.model.CatalogItemDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogItemOptionDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfiguration {

  private static final GraphQLScalarType DATE_TIME = GraphQLScalarType.newScalar()
      .name("DateTime")
      .description("ISO-8061 date time")
      .coercing(new Coercing<OffsetDateTime, String>() {
        @Override
        public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
          return String.valueOf(dataFetcherResult);
        }

        private OffsetDateTime parse(Object input) {
          if (input == null) {
            return null;
          } else if (input instanceof TemporalAccessor) {
            return OffsetDateTime.from((TemporalAccessor) input);
          } else if (input instanceof StringValue) {
            return OffsetDateTime.parse(((StringValue) input).getValue());
          } else {
            return OffsetDateTime.parse(input.toString());
          }
        }

        @Override
        public OffsetDateTime parseValue(Object input) throws CoercingParseValueException {
          return parse(input);
        }

        @Override
        public OffsetDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
          return parse(input);
        }
      })
      .build();

  @Bean
  public SchemaParserDictionary schemaParserDictionary() {
    return new SchemaParserDictionary()
        .add("CatalogItem", CatalogItemDTO.class)
        .add("CatalogItemOption", CatalogItemOptionDTO.class);
  }

  @Bean
  public GraphQLScalarType[] graphQLScalarTypes() {
    return new GraphQLScalarType[] {
        DATE_TIME
    };
  }
}
