package me.kolek.ecommerce.dsgw.api.model.action.order;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderActionResult {
  private String orderId;
  private Status status;
  @Singular
  private List<Reason> reasons;

  public enum Status {
    PENDING,
    SUCCESSFUL,
    FAILED
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  public static class Reason {
    private String description;
  }
}
