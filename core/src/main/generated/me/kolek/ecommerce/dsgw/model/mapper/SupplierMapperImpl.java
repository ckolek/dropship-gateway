package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO;
import me.kolek.ecommerce.dsgw.model.Supplier;
import me.kolek.ecommerce.dsgw.model.Supplier.Status;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class SupplierMapperImpl extends SupplierMapper {

    @Override
    protected SupplierDTO supplierToDto(Supplier supplier, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        SupplierDTO target = context.getMappedInstance( supplier, SupplierDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( supplier == null ) {
            return null;
        }

        SupplierDTO supplierDTO = new SupplierDTO();

        context.storeMappedInstance( supplier, supplierDTO );

        if ( supplier.getId() != null ) {
            supplierDTO.setId( String.valueOf( supplier.getId() ) );
        }
        supplierDTO.setName( supplier.getName() );
        supplierDTO.setStatus( statusToStatus( supplier.getStatus(), context, selection ) );
        supplierDTO.setTimeCreated( supplier.getTimeCreated() );
        supplierDTO.setTimeUpdated( supplier.getTimeUpdated() );

        afterMapping( supplier, supplierDTO, context, selection );

        return supplierDTO;
    }

    protected me.kolek.ecommerce.dsgw.api.model.SupplierDTO.Status statusToStatus(Status status, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        me.kolek.ecommerce.dsgw.api.model.SupplierDTO.Status target = context.getMappedInstance( status, me.kolek.ecommerce.dsgw.api.model.SupplierDTO.Status.class );
        if ( target != null ) {
            return target;
        }

        if ( status == null ) {
            return null;
        }

        me.kolek.ecommerce.dsgw.api.model.SupplierDTO.Status status1;

        switch ( status ) {
            case INACTIVE: status1 = me.kolek.ecommerce.dsgw.api.model.SupplierDTO.Status.INACTIVE;
            break;
            case ACTIVE: status1 = me.kolek.ecommerce.dsgw.api.model.SupplierDTO.Status.ACTIVE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + status );
        }

        context.storeMappedInstance( status, status1 );

        return status1;
    }
}
