package me.kolek.ecommerce.dsgw.test.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.CharStreams;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.test.Auth;
import me.kolek.ecommerce.dsgw.test.Constants;
import me.kolek.ecommerce.dsgw.test.Json;

@RequiredArgsConstructor
public class HttpApi {
  protected final Json json;
  private final Auth.TokenHolder tokenHolder;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  protected <T> ApiResponse<T> invoke(String path, String method, TypeReference<T> responseType)
      throws Exception {
    return invoke(path, (request, body) -> request.method(method, body), null, responseType);
  }

  protected <T> ApiResponse<T> invoke(String path, String method, Object payload,
      TypeReference<T> responseType) throws Exception {
    return invoke(path, (request, body) -> request.method(method, body), payload, responseType);
  }

  protected <T> ApiResponse<T> invoke(String path,
      BiFunction<Builder, BodyPublisher, Builder> applyBodyToRequest, Object payload,
      TypeReference<T> responseType) throws Exception {
    BodyPublisher body = payload != null ? BodyPublishers.ofByteArray(json.toByteArray(payload))
        : BodyPublishers.noBody();

    return invoke(path, r -> applyBodyToRequest.apply(r, body), responseType);
  }

  protected <T> ApiResponse<T> invoke(String path, UnaryOperator<Builder> buildRequest,
      TypeReference<T> responseType) throws Exception {
    return invoke(buildRequest(path, buildRequest), responseType);
  }

  protected HttpRequest buildRequest(String path, UnaryOperator<Builder> build) {
    Builder builder = HttpRequest.newBuilder(Constants.API_BASE_URI.resolve(path));
    tokenHolder.ifSet((token, type) -> builder.header("Authorization", type + " " + token));
    return build.apply(builder).build();
  }

  protected <T> ApiResponse<T> invoke(HttpRequest request, TypeReference<T> responseType)
      throws Exception {
    var response = httpClient.send(request, BodyHandlers.ofInputStream());
    int statusCode = response.statusCode();
    Map<String, List<String>> headers = response.headers().map();

    if (statusCode < 200 || statusCode >= 300) {
      try (var reader = new InputStreamReader(response.body())) {
        throw new ApiResponseException(
            new ApiResponse<>(statusCode, CharStreams.toString(reader), headers));
      }
    }

    try (var inputStream = response.body()) {
      return new ApiResponse<>(statusCode, json.parse(inputStream, responseType), headers);
    }
  }

  protected <T> ApiResponse<T> get(String path, TypeReference<T> responseType) throws Exception {
    return invoke(path, Builder::GET, responseType);
  }

  protected <T> ApiResponse<T> get(String path, Class<T> responseType) throws Exception {
    return get(path, toTypeReference(responseType));
  }

  protected <T> ApiResponse<T> post(String path, Object payload, TypeReference<T> responseType)
      throws Exception {
    return invoke(path, Builder::POST, payload, responseType);
  }

  protected <T> ApiResponse<T> post(String path, Object payload, Class<T> responseType)
      throws Exception {
    return post(path, payload, toTypeReference(responseType));
  }

  protected <T> ApiResponse<T> put(String path, Object payload, TypeReference<T> responseType)
      throws Exception {
    return invoke(path, Builder::PUT, payload, responseType);
  }

  protected <T> ApiResponse<T> put(String path, Object payload, Class<T> responseType)
      throws Exception {
    return put(path, payload, toTypeReference(responseType));
  }

  protected <T> ApiResponse<T> patch(String path, Object payload, TypeReference<T> responseType)
      throws Exception {
    return invoke(path, "PATCH", payload, responseType);
  }

  protected <T> ApiResponse<T> patch(String path, Object payload, Class<T> responseType)
      throws Exception {
    return patch(path, payload, toTypeReference(responseType));
  }

  protected <T> ApiResponse<T> delete(String path, TypeReference<T> responseType) throws Exception {
    return invoke(path, Builder::DELETE, responseType);
  }

  protected <T> ApiResponse<T> delete(String path, Class<T> responseType) throws Exception {
    return delete(path, toTypeReference(responseType));
  }

  protected static <T> TypeReference<T> toTypeReference(Class<T> type) {
    return new TypeReference<T>() {
      @Override
      public Type getType() {
        return type;
      }
    };
  }
}
