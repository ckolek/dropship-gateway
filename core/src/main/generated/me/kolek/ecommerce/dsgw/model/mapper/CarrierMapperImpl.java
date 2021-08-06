package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.CarrierDTO;
import me.kolek.ecommerce.dsgw.model.Carrier;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class CarrierMapperImpl extends CarrierMapper {

    @Override
    public CarrierDTO carrierToDto(Carrier carrier, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        CarrierDTO target = context.getMappedInstance( carrier, CarrierDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( carrier == null ) {
            return null;
        }

        CarrierDTO carrierDTO = new CarrierDTO();

        context.storeMappedInstance( carrier, carrierDTO );

        if ( carrier.getId() != null ) {
            carrierDTO.setId( String.valueOf( carrier.getId() ) );
        }
        carrierDTO.setName( carrier.getName() );
        carrierDTO.setTimeCreated( carrier.getTimeCreated() );
        carrierDTO.setTimeUpdated( carrier.getTimeUpdated() );

        afterMapping( carrier, carrierDTO, context, selection );

        return carrierDTO;
    }
}
