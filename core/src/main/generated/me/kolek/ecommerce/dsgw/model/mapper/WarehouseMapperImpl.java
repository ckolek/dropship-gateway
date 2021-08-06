package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO;
import me.kolek.ecommerce.dsgw.model.Address;
import me.kolek.ecommerce.dsgw.model.Warehouse;
import me.kolek.ecommerce.dsgw.model.Warehouse.Status;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class WarehouseMapperImpl extends WarehouseMapper {

    @Override
    WarehouseDTO warehouseToDto(Warehouse warehouse, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        WarehouseDTO target = context.getMappedInstance( warehouse, WarehouseDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( warehouse == null ) {
            return null;
        }

        WarehouseDTO warehouseDTO = new WarehouseDTO();

        context.storeMappedInstance( warehouse, warehouseDTO );

        if ( warehouse.getId() != null ) {
            warehouseDTO.setId( String.valueOf( warehouse.getId() ) );
        }
        warehouseDTO.setCode( warehouse.getCode() );
        warehouseDTO.setSupplierCode( warehouse.getSupplierCode() );
        warehouseDTO.setDescription( warehouse.getDescription() );
        warehouseDTO.setStatus( statusToStatus( warehouse.getStatus(), context, selection ) );
        warehouseDTO.setAddress( addressToAddressDTO( warehouse.getAddress(), context, selection ) );
        warehouseDTO.setTimeCreated( warehouse.getTimeCreated() );
        warehouseDTO.setTimeUpdated( warehouse.getTimeUpdated() );

        afterMapping( warehouse, warehouseDTO, context, selection );

        return warehouseDTO;
    }

    @Override
    protected List<WarehouseDTO> warehousesToDtoList(Collection<Warehouse> warehouse, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<WarehouseDTO> target = context.getMappedInstance( warehouse, List.class );
        if ( target != null ) {
            return target;
        }

        if ( warehouse == null ) {
            return null;
        }

        List<WarehouseDTO> list = new ArrayList<WarehouseDTO>( warehouse.size() );
        context.storeMappedInstance( warehouse, list );

        for ( Warehouse warehouse1 : warehouse ) {
            list.add( warehouseToDto( warehouse1, context, selection ) );
        }

        return list;
    }

    protected me.kolek.ecommerce.dsgw.api.model.WarehouseDTO.Status statusToStatus(Status status, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        me.kolek.ecommerce.dsgw.api.model.WarehouseDTO.Status target = context.getMappedInstance( status, me.kolek.ecommerce.dsgw.api.model.WarehouseDTO.Status.class );
        if ( target != null ) {
            return target;
        }

        if ( status == null ) {
            return null;
        }

        me.kolek.ecommerce.dsgw.api.model.WarehouseDTO.Status status1;

        switch ( status ) {
            case INACTIVE: status1 = me.kolek.ecommerce.dsgw.api.model.WarehouseDTO.Status.INACTIVE;
            break;
            case ACTIVE: status1 = me.kolek.ecommerce.dsgw.api.model.WarehouseDTO.Status.ACTIVE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + status );
        }

        context.storeMappedInstance( status, status1 );

        return status1;
    }

    protected AddressDTO addressToAddressDTO(Address address, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        AddressDTO target = context.getMappedInstance( address, AddressDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( address == null ) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        context.storeMappedInstance( address, addressDTO );

        addressDTO.setLine1( address.getLine1() );
        addressDTO.setLine2( address.getLine2() );
        addressDTO.setLine3( address.getLine3() );
        addressDTO.setCity( address.getCity() );
        addressDTO.setState( address.getState() );
        addressDTO.setProvince( address.getProvince() );
        addressDTO.setPostalCode( address.getPostalCode() );
        addressDTO.setCountry( address.getCountry() );

        return addressDTO;
    }
}
