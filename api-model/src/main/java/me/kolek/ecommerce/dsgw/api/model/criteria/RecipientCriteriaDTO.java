package me.kolek.ecommerce.dsgw.api.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RecipientCriteriaDTO {
  private ContactCriteriaDTO contact;
  private AddressCriteriaDTO address;
}
