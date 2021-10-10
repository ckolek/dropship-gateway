package me.kolek.ecommerce.dsgw.test.api;

import com.google.common.collect.Iterables;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Value;
import me.kolek.ecommerce.dsgw.test.Auth;
import me.kolek.ecommerce.dsgw.test.Json;

public class GraphQLInvoker extends HttpApi {
  private final Map<String, String> queries;

  public GraphQLInvoker(Json json, Auth.TokenHolder tokenHolder) {
    super(json, tokenHolder);
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
    var response = post("graphql", GraphQLRequest.of(getQuery(query), variables),
        GraphQLResponse.class).body();

    if (response.getErrors() == null || response.getErrors().isEmpty()) {
      if (!response.getData().isEmpty()) {
        Object dataValue = Iterables.getOnlyElement(response.getData().values());
        return json.convert(dataValue, dataType);
      } else {
        return null;
      }
    } else {
      throw new RuntimeException(response.getErrors().toString());
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
