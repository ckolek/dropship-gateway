package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.model.Catalog;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class CatalogMapperImpl extends CatalogMapper {

    @Override
    CatalogDTO catalogToDto(Catalog catalog, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        CatalogDTO target = context.getMappedInstance( catalog, CatalogDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( catalog == null ) {
            return null;
        }

        CatalogDTO catalogDTO = new CatalogDTO();

        context.storeMappedInstance( catalog, catalogDTO );

        if ( catalog.getId() != null ) {
            catalogDTO.setId( String.valueOf( catalog.getId() ) );
        }
        catalogDTO.setExternalId( catalog.getExternalId() );
        catalogDTO.setDescription( catalog.getDescription() );
        catalogDTO.setTimeCreated( catalog.getTimeCreated() );
        catalogDTO.setTimeUpdated( catalog.getTimeUpdated() );

        afterMapping( catalog, catalogDTO, context, selection );

        return catalogDTO;
    }
}
