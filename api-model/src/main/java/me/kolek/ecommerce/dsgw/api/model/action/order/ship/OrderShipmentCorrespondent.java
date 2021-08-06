package me.kolek.ecommerce.dsgw.api.model.action.order.ship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderShipmentCorrespondent {
  private ContactDTO contact;
  private AddressDTO address;
}
