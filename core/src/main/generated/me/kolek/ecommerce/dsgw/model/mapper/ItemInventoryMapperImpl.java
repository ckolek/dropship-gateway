package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.InventoryDTO;
import me.kolek.ecommerce.dsgw.model.ItemInventory;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ItemInventoryMapperImpl extends ItemInventoryMapper {

    @Override
    InventoryDTO itemInventoryToDto(ItemInventory itemInventory, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        InventoryDTO target = context.getMappedInstance( itemInventory, InventoryDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( itemInventory == null ) {
            return null;
        }

        InventoryDTO inventoryDTO = new InventoryDTO();

        context.storeMappedInstance( itemInventory, inventoryDTO );

        if ( itemInventory.getId() != null ) {
            inventoryDTO.setId( String.valueOf( itemInventory.getId() ) );
        }
        inventoryDTO.setQuantityAvailable( itemInventory.getQuantityAvailable() );
        inventoryDTO.setTimeCreated( itemInventory.getTimeCreated() );
        inventoryDTO.setTimeUpdated( itemInventory.getTimeUpdated() );

        afterMapping( itemInventory, inventoryDTO, context, selection );

        return inventoryDTO;
    }
}
