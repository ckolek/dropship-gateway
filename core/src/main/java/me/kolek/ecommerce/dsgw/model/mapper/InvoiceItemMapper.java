package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.InvoiceItemDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.model.InvoiceItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {InvoiceMapper.class,
    OrderItemMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class InvoiceItemMapper {

  public static final String FIELD__INVOICE = "invoice";
  public static final String FIELD__ORDER_ITEM = "orderItem";

  @Inject
  private InvoiceMapper invoiceMapper;

  @Inject
  private OrderItemMapper orderItemMapper;

  @Mapping(target = FIELD__INVOICE, ignore = true)
  @Mapping(target = FIELD__ORDER_ITEM, ignore = true)
  public abstract InvoiceItemDTO invoiceItemToDto(InvoiceItem invoiceItem,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(InvoiceItem invoiceItem, @MappingTarget InvoiceItemDTO invoiceItemDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__INVOICE, subSelection -> invoiceItemDTO
        .setInvoice(invoiceMapper.invoiceToDto(invoiceItem.getInvoice(), context, subSelection)));
    mapIfSelected(selection, FIELD__ORDER_ITEM, subSelection -> invoiceItemDTO.setOrderItem(
        orderItemMapper.orderItemToDto(invoiceItem.getOrderItem(), context, subSelection)));
  }
}
