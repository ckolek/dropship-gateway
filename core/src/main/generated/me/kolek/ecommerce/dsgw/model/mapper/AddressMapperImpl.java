package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.api.model.AddressDTO.AddressDTOBuilder;
import me.kolek.ecommerce.dsgw.model.Address;
import me.kolek.ecommerce.dsgw.model.Address.AddressBuilder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressDTO addressToDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTOBuilder addressDTO = AddressDTO.builder();

        addressDTO.line1( address.getLine1() );
        addressDTO.line2( address.getLine2() );
        addressDTO.line3( address.getLine3() );
        addressDTO.city( address.getCity() );
        addressDTO.state( address.getState() );
        addressDTO.province( address.getProvince() );
        addressDTO.postalCode( address.getPostalCode() );
        addressDTO.country( address.getCountry() );

        return addressDTO.build();
    }

    @Override
    public Address addressFromDto(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        AddressBuilder address = Address.builder();

        address.line1( addressDTO.getLine1() );
        address.line2( addressDTO.getLine2() );
        address.line3( addressDTO.getLine3() );
        address.city( addressDTO.getCity() );
        address.state( addressDTO.getState() );
        address.province( addressDTO.getProvince() );
        address.postalCode( addressDTO.getPostalCode() );
        address.country( addressDTO.getCountry() );

        return address.build();
    }
}
