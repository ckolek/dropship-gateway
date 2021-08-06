package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.InvoiceItemDTO;
import me.kolek.ecommerce.dsgw.model.InvoiceItem;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class InvoiceItemMapperImpl extends InvoiceItemMapper {

    @Override
    public InvoiceItemDTO invoiceItemToDto(InvoiceItem invoiceItem, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        InvoiceItemDTO target = context.getMappedInstance( invoiceItem, InvoiceItemDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( invoiceItem == null ) {
            return null;
        }

        InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO();

        context.storeMappedInstance( invoiceItem, invoiceItemDTO );

        if ( invoiceItem.getId() != null ) {
            invoiceItemDTO.setId( String.valueOf( invoiceItem.getId() ) );
        }
        invoiceItemDTO.setQuantity( invoiceItem.getQuantity() );
        invoiceItemDTO.setTimeCreated( invoiceItem.getTimeCreated() );
        invoiceItemDTO.setTimeUpdated( invoiceItem.getTimeUpdated() );

        afterMapping( invoiceItem, invoiceItemDTO, context, selection );

        return invoiceItemDTO;
    }

    @Override
    protected List<InvoiceItemDTO> invoiceItemsToDtoList(Collection<InvoiceItem> items, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<InvoiceItemDTO> target = context.getMappedInstance( items, List.class );
        if ( target != null ) {
            return target;
        }

        if ( items == null ) {
            return null;
        }

        List<InvoiceItemDTO> list = new ArrayList<InvoiceItemDTO>( items.size() );
        context.storeMappedInstance( items, list );

        for ( InvoiceItem invoiceItem : items ) {
            list.add( invoiceItemToDto( invoiceItem, context, selection ) );
        }

        return list;
    }
}
