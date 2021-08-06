package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO.ContactDTOBuilder;
import me.kolek.ecommerce.dsgw.model.Contact;
import me.kolek.ecommerce.dsgw.model.Contact.ContactBuilder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public ContactDTO contactToDto(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDTOBuilder contactDTO = ContactDTO.builder();

        contactDTO.name( contact.getName() );
        contactDTO.phone( contact.getPhone() );
        contactDTO.email( contact.getEmail() );

        return contactDTO.build();
    }

    @Override
    public Contact contactFromDto(ContactDTO contactDTO) {
        if ( contactDTO == null ) {
            return null;
        }

        ContactBuilder contact = Contact.builder();

        contact.name( contactDTO.getName() );
        contact.phone( contactDTO.getPhone() );
        contact.email( contactDTO.getEmail() );

        return contact.build();
    }
}
