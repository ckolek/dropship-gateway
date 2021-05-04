package me.kolek.ecommerce.dsgw.api.model.action.order.submit;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SubmitOrderItem {
  private String sku;
  private String gtin;
  private String upc;
  private String ean;
  private String isbn;
  private Integer quantity;
  private String customization;
  private OffsetDateTime expectedShipDate;
  private OffsetDateTime expectedDeliveryDate;
}
