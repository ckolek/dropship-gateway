package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.InvoiceDTO;
import me.kolek.ecommerce.dsgw.model.Invoice;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class InvoiceMapperImpl extends InvoiceMapper {

    @Override
    public InvoiceDTO invoiceToDto(Invoice invoice, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        InvoiceDTO target = context.getMappedInstance( invoice, InvoiceDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( invoice == null ) {
            return null;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();

        context.storeMappedInstance( invoice, invoiceDTO );

        if ( invoice.getId() != null ) {
            invoiceDTO.setId( String.valueOf( invoice.getId() ) );
        }
        invoiceDTO.setTimeCreated( invoice.getTimeCreated() );
        invoiceDTO.setTimeUpdated( invoice.getTimeUpdated() );

        afterMapping( invoice, invoiceDTO, context, selection );

        return invoiceDTO;
    }

    @Override
    protected List<InvoiceDTO> invoicesToDtoList(Collection<Invoice> invoices, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<InvoiceDTO> target = context.getMappedInstance( invoices, List.class );
        if ( target != null ) {
            return target;
        }

        if ( invoices == null ) {
            return null;
        }

        List<InvoiceDTO> list = new ArrayList<InvoiceDTO>( invoices.size() );
        context.storeMappedInstance( invoices, list );

        for ( Invoice invoice : invoices ) {
            list.add( invoiceToDto( invoice, context, selection ) );
        }

        return list;
    }
}
