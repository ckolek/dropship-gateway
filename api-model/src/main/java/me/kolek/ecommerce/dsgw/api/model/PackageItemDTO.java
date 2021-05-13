package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class PackageItemDTO {
  @Getter
  @Setter
  private String id;
  private PackageDTO _package;
  @Getter
  @Setter
  private OrderItemDTO orderItem;
  @Getter
  @Setter
  private Integer quantity;
  @Getter
  @Setter
  private OffsetDateTime timeCreated;
  @Getter
  @Setter
  private OffsetDateTime timeUpdated;

  public PackageDTO getPackage() {
    return _package;
  }

  public void setPackage(PackageDTO _package) {
    this._package = _package;
  }
}
