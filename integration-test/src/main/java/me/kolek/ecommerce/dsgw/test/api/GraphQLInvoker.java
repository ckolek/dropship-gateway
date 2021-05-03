package me.kolek.ecommerce.dsgw.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Value;
import me.kolek.ecommerce.dsgw.test.Constants;

public class GraphQLInvoker {
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  private final Map<String, String> queries;

  public GraphQLInvoker() {
    this.httpClient = HttpClient.newHttpClient();
    this.objectMapper = new ObjectMapper().findAndRegisterModules();
    this.queries = new HashMap<>();
  }

  private String loadQuery(String name) throws Exception {
    var resource = getClass().getResource("graphql/" + name + ".graphql");
    return Files.readString(Path.of(resource.toURI()));
  }

  private String getQuery(String name) throws Exception {
    synchronized (queries) {
      String query = queries.get(name);
      if (query == null) {
        queries.put(name, query = loadQuery(name));
      }
      return query;
    }
  }

  public <T> T invoke(String query, Map<String, Object> variables, Class<T> dataType)
      throws Exception {
    byte[] body = objectMapper.writer()
        .writeValueAsBytes(GraphQLRequest.of(getQuery(query), variables));

    var request = HttpRequest.newBuilder(Constants.API_BASE_URI.resolve("graphql"))
        .POST(BodyPublishers.ofByteArray(body))
        .build();

    var response = httpClient.send(request, BodyHandlers.ofInputStream());

    GraphQLResponse graphQLResponse;
    try (var inputStream = response.body()) {
      graphQLResponse = objectMapper.readValue(inputStream, GraphQLResponse.class);
    }

    if (graphQLResponse.getErrors() == null || graphQLResponse.getErrors().isEmpty()) {
      if (!graphQLResponse.getData().isEmpty()) {
        Object dataValue = Iterables.getOnlyElement(graphQLResponse.getData().values());
        return objectMapper.convertValue(dataValue, dataType);
      } else {
        return null;
      }
    } else {
      throw new RuntimeException(graphQLResponse.getErrors().toString());
    }
  }

  @Value(staticConstructor = "of")
  private static class GraphQLRequest {
    String query;
    Map<String, Object> variables;
  }

  @Data
  private static class GraphQLResponse {
    private Map<String, Object> data;
    private List<Object> errors;
  }
}