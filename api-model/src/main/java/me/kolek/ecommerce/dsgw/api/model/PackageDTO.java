package me.kolek.ecommerce.dsgw.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PackageDTO {

  private String id;
  private OrderDTO order;
  private WarehouseDTO warehouse;
  private ContactDTO senderContact;
  private AddressDTO senderAddress;
  private ContactDTO recipientContact;
  private AddressDTO recipientAddress;
  private ServiceLevelDTO serviceLevel;
  private String trackingNumber;
  private List<PackageItemDTO> items;
  private OffsetDateTime timeShipped;
  private OffsetDateTime timeCreated;
  private OffsetDateTime timeUpdated;
}
