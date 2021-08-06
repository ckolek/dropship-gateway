package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.model.CatalogItem;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class CatalogEntryMapperImpl extends CatalogEntryMapper {

    @Override
    protected CatalogEntryDTO catalogItemToDto(CatalogItem catalogItem, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        CatalogEntryDTO target = context.getMappedInstance( catalogItem, CatalogEntryDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( catalogItem == null ) {
            return null;
        }

        CatalogEntryDTO catalogEntryDTO = new CatalogEntryDTO();

        context.storeMappedInstance( catalogItem, catalogEntryDTO );

        if ( catalogItem.getId() != null ) {
            catalogEntryDTO.setId( String.valueOf( catalogItem.getId() ) );
        }
        catalogEntryDTO.setName( catalogItem.getName() );
        catalogEntryDTO.setShortDescription( catalogItem.getShortDescription() );
        catalogEntryDTO.setLongDescription( catalogItem.getLongDescription() );
        catalogEntryDTO.setSku( catalogItem.getSku() );
        catalogEntryDTO.setMpn( catalogItem.getMpn() );
        catalogEntryDTO.setGtin( catalogItem.getGtin() );
        catalogEntryDTO.setUpc( catalogItem.getUpc() );
        catalogEntryDTO.setEan( catalogItem.getEan() );
        catalogEntryDTO.setIsbn( catalogItem.getIsbn() );
        catalogEntryDTO.setManufacturer( catalogItem.getManufacturer() );
        catalogEntryDTO.setBrand( catalogItem.getBrand() );
        catalogEntryDTO.setTimeCreated( catalogItem.getTimeCreated() );
        catalogEntryDTO.setTimeUpdated( catalogItem.getTimeUpdated() );

        afterMapping( catalogItem, catalogEntryDTO, context, selection );

        return catalogEntryDTO;
    }

    @Override
    protected List<CatalogEntryDTO> catalogItemsToDtoList(Collection<CatalogItem> catalogItems, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<CatalogEntryDTO> target = context.getMappedInstance( catalogItems, List.class );
        if ( target != null ) {
            return target;
        }

        if ( catalogItems == null ) {
            return null;
        }

        List<CatalogEntryDTO> list = new ArrayList<CatalogEntryDTO>( catalogItems.size() );
        context.storeMappedInstance( catalogItems, list );

        for ( CatalogItem catalogItem : catalogItems ) {
            list.add( catalogItemToDto( catalogItem, context, selection ) );
        }

        return list;
    }
}
