package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InvoiceDTO {

  private String id;
  private OrderDTO order;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<InvoiceItemDTO> items;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
