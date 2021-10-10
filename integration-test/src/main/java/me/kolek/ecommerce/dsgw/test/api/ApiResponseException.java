package me.kolek.ecommerce.dsgw.test.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ApiResponseException extends Exception {
  private final ApiResponse<String> response;

  public ApiResponseException(ApiResponse<String> response) {
    super("status " + response.status() + ": " + response.body());
    this.response = response;
  }
}
