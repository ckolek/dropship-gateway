package me.kolek.ecommerce.dsgw.model.mapper;

import me.kolek.ecommerce.dsgw.api.model.OrderCancelCodeDTO;
import me.kolek.ecommerce.dsgw.model.OrderCancelCode;
import org.mapstruct.Mapper;

@Mapper
public interface OrderCancelCodeMapper {

  OrderCancelCodeDTO orderCancelCodeToDto(OrderCancelCode orderCancelCode);
}
