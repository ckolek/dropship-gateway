package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.OrderCancelCodeDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderCancelCodeDTO.OrderCancelCodeDTOBuilder;
import me.kolek.ecommerce.dsgw.model.OrderCancelCode;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class OrderCancelCodeMapperImpl implements OrderCancelCodeMapper {

    @Override
    public OrderCancelCodeDTO orderCancelCodeToDto(OrderCancelCode orderCancelCode) {
        if ( orderCancelCode == null ) {
            return null;
        }

        OrderCancelCodeDTOBuilder orderCancelCodeDTO = OrderCancelCodeDTO.builder();

        if ( orderCancelCode.getId() != null ) {
            orderCancelCodeDTO.id( String.valueOf( orderCancelCode.getId() ) );
        }
        orderCancelCodeDTO.code( orderCancelCode.getCode() );
        orderCancelCodeDTO.description( orderCancelCode.getDescription() );
        orderCancelCodeDTO.timeCreated( orderCancelCode.getTimeCreated() );
        orderCancelCodeDTO.timeUpdated( orderCancelCode.getTimeUpdated() );

        return orderCancelCodeDTO.build();
    }
}
