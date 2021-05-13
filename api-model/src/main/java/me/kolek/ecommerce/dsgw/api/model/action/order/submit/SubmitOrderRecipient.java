package me.kolek.ecommerce.dsgw.api.model.action.order.submit;

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
public class SubmitOrderRecipient {
  private ContactDTO contact;
  private AddressDTO address;
}
