package me.kolek.ecommerce.dsgw.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.InputStream;
import lombok.SneakyThrows;

public class Json {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

  @SneakyThrows
  public <T> T parse(String string, TypeReference<T> type) {
    return objectMapper.readValue(string, type);
  }

  @SneakyThrows
  public <T> T parse(String string, Class<T> type) {
    return objectMapper.readValue(string, type);
  }

  @SneakyThrows
  public <T> T parse(InputStream inputStream, TypeReference<T> type) {
    return objectMapper.readValue(inputStream, type);
  }

  @SneakyThrows
  public <T> T parse(InputStream inputStream, Class<T> type) {
    return objectMapper.readValue(inputStream, type);
  }

  @SneakyThrows
  public byte[] toByteArray(Object object) {
    return objectMapper.writeValueAsBytes(object);
  }

  @SneakyThrows
  public String toString(Object object) {
    return objectMapper.writeValueAsString(object);
  }

  @SneakyThrows
  public <T> T convert(Object object, Class<T> type) {
    return objectMapper.convertValue(object, type);
  }
}
