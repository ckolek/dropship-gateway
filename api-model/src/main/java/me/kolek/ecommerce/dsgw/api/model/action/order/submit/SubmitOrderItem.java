package me.kolek.ecommerce.dsgw.api.model.action.order.submit;

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
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SubmitOrderItem {
  private String sku;
  private String gtin;
  private String upc;
  private String ean;
  private String isbn;
  private Integer quantity;
  private String customization;
  OffsetDateTime expectedShipDate;
  OffsetDateTime expectedDeliveryDate;
}
