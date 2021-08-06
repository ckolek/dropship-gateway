package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.PackageItemDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.ship.OrderShipmentItem;
import me.kolek.ecommerce.dsgw.model.PackageItem;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class PackageItemMapperImpl extends PackageItemMapper {

    @Override
    public PackageItemDTO packageItemToDto(PackageItem packageItem, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        PackageItemDTO target = context.getMappedInstance( packageItem, PackageItemDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( packageItem == null ) {
            return null;
        }

        PackageItemDTO packageItemDTO = new PackageItemDTO();

        context.storeMappedInstance( packageItem, packageItemDTO );

        if ( packageItem.getId() != null ) {
            packageItemDTO.setId( String.valueOf( packageItem.getId() ) );
        }
        packageItemDTO.setQuantity( packageItem.getQuantity() );
        packageItemDTO.setTimeCreated( packageItem.getTimeCreated() );
        packageItemDTO.setTimeUpdated( packageItem.getTimeUpdated() );

        afterMapping( packageItem, packageItemDTO, context, selection );

        return packageItemDTO;
    }

    @Override
    protected List<PackageItemDTO> packageItemToDtoList(Collection<PackageItem> items, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<PackageItemDTO> target = context.getMappedInstance( items, List.class );
        if ( target != null ) {
            return target;
        }

        if ( items == null ) {
            return null;
        }

        List<PackageItemDTO> list = new ArrayList<PackageItemDTO>( items.size() );
        context.storeMappedInstance( items, list );

        for ( PackageItem packageItem : items ) {
            list.add( packageItemToDto( packageItem, context, selection ) );
        }

        return list;
    }

    @Override
    public PackageItem orderShipmentItemToPackageItem(OrderShipmentItem item) {
        if ( item == null ) {
            return null;
        }

        PackageItem packageItem = new PackageItem();

        packageItem.setQuantity( item.getQuantity() );

        return packageItem;
    }
}
