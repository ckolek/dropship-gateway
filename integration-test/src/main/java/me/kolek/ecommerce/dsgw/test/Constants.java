package me.kolek.ecommerce.dsgw.test;

import java.net.URI;
import java.util.Optional;

public class Constants {

  public static final URI API_BASE_URI = URI.create(
      Optional.ofNullable(System.getenv("api.baseUrl")).orElse("http://localhost:8080/api/"));
}
