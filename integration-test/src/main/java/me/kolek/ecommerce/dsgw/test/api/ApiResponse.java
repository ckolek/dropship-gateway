package me.kolek.ecommerce.dsgw.test.api;

import java.util.List;
import java.util.Map;

public record ApiResponse<T>(int status, T body, Map<String, List<String>> headers) {
}
