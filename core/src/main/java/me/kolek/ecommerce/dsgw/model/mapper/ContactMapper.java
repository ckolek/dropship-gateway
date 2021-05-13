package me.kolek.ecommerce.dsgw.model.mapper;

import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.model.Contact;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ContactMapper {

  ContactDTO contactToDto(Contact contact);

  @InheritInverseConfiguration
  Contact contactFromDto(ContactDTO contactDTO);
}
