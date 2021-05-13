package me.kolek.ecommerce.dsgw.model.mapper;

import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.model.Address;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {

  AddressDTO addressToDto(Address address);

  @InheritInverseConfiguration
  Address addressFromDto(AddressDTO addressDTO);
}
