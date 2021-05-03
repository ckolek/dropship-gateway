package me.kolek.ecommerce.dsgw.model.mapper;

import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.model.Contact;
import org.mapstruct.Mapper;

@Mapper
public interface ContactMapper {

  ContactDTO contactToDto(Contact contact);
}
