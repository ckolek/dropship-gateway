package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.OrderItemDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderItem;
import me.kolek.ecommerce.dsgw.model.Order.Status;
import me.kolek.ecommerce.dsgw.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class OrderItemMapperImpl extends OrderItemMapper {

    @Autowired
    private OrderCancelCodeMapper orderCancelCodeMapper;

    @Override
    OrderItemDTO orderItemToDto(OrderItem orderItem, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        OrderItemDTO target = context.getMappedInstance( orderItem, OrderItemDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( orderItem == null ) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();

        context.storeMappedInstance( orderItem, orderItemDTO );

        if ( orderItem.getId() != null ) {
            orderItemDTO.setId( String.valueOf( orderItem.getId() ) );
        }
        orderItemDTO.setLineNumber( orderItem.getLineNumber() );
        orderItemDTO.setQuantity( orderItem.getQuantity() );
        orderItemDTO.setCustomization( orderItem.getCustomization() );
        orderItemDTO.setExpectedShipDate( orderItem.getExpectedShipDate() );
        orderItemDTO.setExpectedDeliveryDate( orderItem.getExpectedDeliveryDate() );
        orderItemDTO.setStatus( statusToStatus( orderItem.getStatus(), context, selection ) );
        orderItemDTO.setQuantityAccepted( orderItem.getQuantityAccepted() );
        orderItemDTO.setQuantityRejected( orderItem.getQuantityRejected() );
        orderItemDTO.setRejectCode( orderCancelCodeMapper.orderCancelCodeToDto( orderItem.getRejectCode() ) );
        orderItemDTO.setRejectReason( orderItem.getRejectReason() );
        orderItemDTO.setTimeAcknowledged( orderItem.getTimeAcknowledged() );
        orderItemDTO.setQuantityCancelled( orderItem.getQuantityCancelled() );
        orderItemDTO.setCancelCode( orderCancelCodeMapper.orderCancelCodeToDto( orderItem.getCancelCode() ) );
        orderItemDTO.setCancelReason( orderItem.getCancelReason() );
        orderItemDTO.setTimeCancelled( orderItem.getTimeCancelled() );
        orderItemDTO.setTimeCreated( orderItem.getTimeCreated() );
        orderItemDTO.setTimeUpdated( orderItem.getTimeUpdated() );

        afterMapping( orderItem, orderItemDTO, context, selection );

        return orderItemDTO;
    }

    @Override
    protected List<OrderItemDTO> orderItemsToDtoList(Collection<OrderItem> orderItems, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        List<OrderItemDTO> target = context.getMappedInstance( orderItems, List.class );
        if ( target != null ) {
            return target;
        }

        if ( orderItems == null ) {
            return null;
        }

        List<OrderItemDTO> list = new ArrayList<OrderItemDTO>( orderItems.size() );
        context.storeMappedInstance( orderItems, list );

        for ( OrderItem orderItem : orderItems ) {
            list.add( orderItemToDto( orderItem, context, selection ) );
        }

        return list;
    }

    @Override
    public OrderItem submitOrderItemToOrderItem(SubmitOrderItem submitOrderItem) {
        if ( submitOrderItem == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity( submitOrderItem.getQuantity() );
        orderItem.setCustomization( submitOrderItem.getCustomization() );
        orderItem.setExpectedShipDate( submitOrderItem.getExpectedShipDate() );
        orderItem.setExpectedDeliveryDate( submitOrderItem.getExpectedDeliveryDate() );

        orderItem.setStatus( Status.NEW );

        return orderItem;
    }

    protected me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status statusToStatus(Status status, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status target = context.getMappedInstance( status, me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.class );
        if ( target != null ) {
            return target;
        }

        if ( status == null ) {
            return null;
        }

        me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status status1;

        switch ( status ) {
            case NEW: status1 = me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.NEW;
            break;
            case ACKNOWLEDGED: status1 = me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.ACKNOWLEDGED;
            break;
            case SHIPPED_PARTIAL: status1 = me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.SHIPPED_PARTIAL;
            break;
            case SHIPPED: status1 = me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.SHIPPED;
            break;
            case INVOICED_PARTIAL: status1 = me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.INVOICED_PARTIAL;
            break;
            case INVOICED: status1 = me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.INVOICED;
            break;
            case CANCELLED: status1 = me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status.CANCELLED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + status );
        }

        context.storeMappedInstance( status, status1 );

        return status1;
    }
}
