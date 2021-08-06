package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.api.model.PackageDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentCorrespondent;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentRequest;
import me.kolek.ecommerce.dsgw.model.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class PackageMapperImpl extends PackageMapper {

    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ServiceLevelMapper serviceLevelMapper;

    @Override
    public PackageDTO packageToDto(Package _package, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        PackageDTO target = context.getMappedInstance( _package, PackageDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( _package == null ) {
            return null;
        }

        PackageDTO packageDTO = new PackageDTO();

        context.storeMappedInstance( _package, packageDTO );

        packageDTO.setManifestId( _package.getExternalId() );
        if ( _package.getId() != null ) {
            packageDTO.setId( String.valueOf( _package.getId() ) );
        }
        packageDTO.setSenderContact( contactMapper.contactToDto( _package.getSenderContact() ) );
        packageDTO.setSenderAddress( addressMapper.addressToDto( _package.getSenderAddress() ) );
        packageDTO.setRecipientContact( contactMapper.contactToDto( _package.getRecipientContact() ) );
        packageDTO.setRecipientAddress( addressMapper.addressToDto( _package.getRecipientAddress() ) );
        packageDTO.setServiceLevel( serviceLevelMapper.serviceLevelToDto( _package.getServiceLevel(), context, selection ) );
        packageDTO.setTrackingNumber( _package.getTrackingNumber() );
        packageDTO.setTimeShipped( _package.getTimeShipped() );
        packageDTO.setTimeCreated( _package.getTimeCreated() );
        packageDTO.setTimeUpdated( _package.getTimeUpdated() );

        afterMapping( _package, packageDTO, context, selection );

        return packageDTO;
    }

    @Override
    protected List<PackageDTO> packagesToDtoList(Collection<Package> packages, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<PackageDTO> target = context.getMappedInstance( packages, List.class );
        if ( target != null ) {
            return target;
        }

        if ( packages == null ) {
            return null;
        }

        List<PackageDTO> list = new ArrayList<PackageDTO>( packages.size() );
        context.storeMappedInstance( packages, list );

        for ( Package package1 : packages ) {
            list.add( packageToDto( package1, context, selection ) );
        }

        return list;
    }

    @Override
    public Package orderShipmentRequestToPackage(OrderShipmentRequest request) {
        if ( request == null ) {
            return null;
        }

        Package package1 = new Package();

        package1.setExternalId( request.getManifestId() );
        package1.setSenderContact( contactMapper.contactFromDto( requestSenderContact( request ) ) );
        package1.setSenderAddress( addressMapper.addressFromDto( requestSenderAddress( request ) ) );
        package1.setRecipientContact( contactMapper.contactFromDto( requestRecipientContact( request ) ) );
        package1.setRecipientAddress( addressMapper.addressFromDto( requestRecipientAddress( request ) ) );
        package1.setTrackingNumber( request.getTrackingNumber() );
        package1.setTimeShipped( request.getTimeShipped() );

        return package1;
    }

    private ContactDTO requestSenderContact(OrderShipmentRequest orderShipmentRequest) {
        if ( orderShipmentRequest == null ) {
            return null;
        }
        OrderShipmentCorrespondent sender = orderShipmentRequest.getSender();
        if ( sender == null ) {
            return null;
        }
        ContactDTO contact = sender.getContact();
        if ( contact == null ) {
            return null;
        }
        return contact;
    }

    private AddressDTO requestSenderAddress(OrderShipmentRequest orderShipmentRequest) {
        if ( orderShipmentRequest == null ) {
            return null;
        }
        OrderShipmentCorrespondent sender = orderShipmentRequest.getSender();
        if ( sender == null ) {
            return null;
        }
        AddressDTO address = sender.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }

    private ContactDTO requestRecipientContact(OrderShipmentRequest orderShipmentRequest) {
        if ( orderShipmentRequest == null ) {
            return null;
        }
        OrderShipmentCorrespondent recipient = orderShipmentRequest.getRecipient();
        if ( recipient == null ) {
            return null;
        }
        ContactDTO contact = recipient.getContact();
        if ( contact == null ) {
            return null;
        }
        return contact;
    }

    private AddressDTO requestRecipientAddress(OrderShipmentRequest orderShipmentRequest) {
        if ( orderShipmentRequest == null ) {
            return null;
        }
        OrderShipmentCorrespondent recipient = orderShipmentRequest.getRecipient();
        if ( recipient == null ) {
            return null;
        }
        AddressDTO address = recipient.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }
}
