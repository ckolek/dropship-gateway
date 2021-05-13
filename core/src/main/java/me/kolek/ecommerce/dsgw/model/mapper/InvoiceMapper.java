package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.InvoiceDTO;
import me.kolek.ecommerce.dsgw.api.model.InvoiceItemDTO;
import me.kolek.ecommerce.dsgw.model.Invoice;
import me.kolek.ecommerce.dsgw.model.InvoiceItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {OrderMapper.class,
    InvoiceItemMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class InvoiceMapper {

  public static final String FIELD__ORDER = "order";
  public static final String FIELD__ITEMS = "items";

  @Inject
  private OrderMapper orderMapper;

  @Mapping(target = FIELD__ORDER, ignore = true)
  @Mapping(target = FIELD__ITEMS, ignore = true)
  public abstract InvoiceDTO invoiceToDto(Invoice invoice,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Invoice invoice, @MappingTarget InvoiceDTO invoiceDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__ORDER, subSelection -> invoiceDTO
        .setOrder(orderMapper.orderToDto(invoice.getOrder(), context, subSelection)));
    mapIfSelected(selection, FIELD__ITEMS, subSelection -> invoiceDTO
        .setItems(invoiceItemToDtoList(invoice.getItems(), context, subSelection)));

    if (context.isSetParentReferences() && invoiceDTO.getItems() != null) {
      invoiceDTO.getItems().forEach(item -> item.setInvoice(invoiceDTO));
    }
  }

  protected abstract List<InvoiceItemDTO> invoiceItemToDtoList(List<InvoiceItem> items,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);
}
