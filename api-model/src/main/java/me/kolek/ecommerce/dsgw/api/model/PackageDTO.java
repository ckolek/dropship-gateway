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
public class PackageDTO {

  private String id;
  private String manifestId;
  private OrderDTO order;
  private WarehouseDTO warehouse;
  private ContactDTO senderContact;
  private AddressDTO senderAddress;
  private ContactDTO recipientContact;
  private AddressDTO recipientAddress;
  private ServiceLevelDTO serviceLevel;
  private String trackingNumber;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<PackageItemDTO> items;
  private OffsetDateTime timeShipped;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
