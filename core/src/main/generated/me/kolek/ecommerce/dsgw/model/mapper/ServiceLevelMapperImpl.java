package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelDTO;
import me.kolek.ecommerce.dsgw.model.ServiceLevel;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ServiceLevelMapperImpl extends ServiceLevelMapper {

    @Override
    ServiceLevelDTO serviceLevelToDto(ServiceLevel serviceLevel, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        ServiceLevelDTO target = context.getMappedInstance( serviceLevel, ServiceLevelDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( serviceLevel == null ) {
            return null;
        }

        ServiceLevelDTO serviceLevelDTO = new ServiceLevelDTO();

        context.storeMappedInstance( serviceLevel, serviceLevelDTO );

        if ( serviceLevel.getId() != null ) {
            serviceLevelDTO.setId( String.valueOf( serviceLevel.getId() ) );
        }
        serviceLevelDTO.setMode( serviceLevel.getMode() );
        serviceLevelDTO.setCode( serviceLevel.getCode() );
        serviceLevelDTO.setTimeCreated( serviceLevel.getTimeCreated() );
        serviceLevelDTO.setTimeUpdated( serviceLevel.getTimeUpdated() );

        afterMapping( serviceLevel, serviceLevelDTO, context, selection );

        return serviceLevelDTO;
    }

    @Override
    protected List<ServiceLevelDTO> serviceLevelsToDtoList(Collection<ServiceLevel> serviceLevels, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<ServiceLevelDTO> target = context.getMappedInstance( serviceLevels, List.class );
        if ( target != null ) {
            return target;
        }

        if ( serviceLevels == null ) {
            return null;
        }

        List<ServiceLevelDTO> list = new ArrayList<ServiceLevelDTO>( serviceLevels.size() );
        context.storeMappedInstance( serviceLevels, list );

        for ( ServiceLevel serviceLevel : serviceLevels ) {
            list.add( serviceLevelToDto( serviceLevel, context, selection ) );
        }

        return list;
    }
}
