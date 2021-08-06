package me.kolek.ecommerce.dsgw.events.handler;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonUtil {
  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper()
        .findAndRegisterModules()
        .setDefaultPropertyInclusion(Include.NON_NULL)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
