package me.kolek.ecommerce.dsgw.model.mapper;

import javax.annotation.processing.Generated;
import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.RecipientDTO;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRecipient;
import me.kolek.ecommerce.dsgw.api.model.action.order.submit.SubmitOrderRequest;
import me.kolek.ecommerce.dsgw.model.Order;
import me.kolek.ecommerce.dsgw.model.Order.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class OrderMapperImpl extends OrderMapper {

    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private OrderCancelCodeMapper orderCancelCodeMapper;

    @Override
    OrderDTO orderToDto(Order order, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        OrderDTO target = context.getMappedInstance( order, OrderDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        context.storeMappedInstance( order, orderDTO );

        orderDTO.setRecipient( orderToRecipientDTO( order, context, selection ) );
        orderDTO.setOrderNumber( order.getExternalId() );
        if ( order.getId() != null ) {
            orderDTO.setId( String.valueOf( order.getId() ) );
        }
        orderDTO.setCustomerOrderNumber( order.getCustomerOrderNumber() );
        orderDTO.setStatus( statusToStatus( order.getStatus(), context, selection ) );
        orderDTO.setCancelCode( orderCancelCodeMapper.orderCancelCodeToDto( order.getCancelCode() ) );
        orderDTO.setCancelReason( order.getCancelReason() );
        orderDTO.setTimeOrdered( order.getTimeOrdered() );
        orderDTO.setTimeReleased( order.getTimeReleased() );
        orderDTO.setTimeAcknowledged( order.getTimeAcknowledged() );
        orderDTO.setTimeCancelled( order.getTimeCancelled() );
        orderDTO.setTimeCreated( order.getTimeCreated() );
        orderDTO.setTimeUpdated( order.getTimeUpdated() );

        afterMapping( order, orderDTO, context, selection );

        return orderDTO;
    }

    @Override
    public Order submitOrderRequestToOrder(SubmitOrderRequest request, Order order) {
        if ( request == null ) {
            return null;
        }

        order.setExternalId( request.getOrderNumber() );
        order.setContact( contactMapper.contactFromDto( requestRecipientContact( request ) ) );
        order.setAddress( addressMapper.addressFromDto( requestRecipientAddress( request ) ) );
        order.setCustomerOrderNumber( request.getCustomerOrderNumber() );
        order.setTimeOrdered( request.getTimeOrdered() );
        order.setTimeReleased( request.getTimeReleased() );

        order.setStatus( Status.NEW );

        return order;
    }

    protected RecipientDTO orderToRecipientDTO(Order order, CycleAvoidingMappingContext context, MappingFieldSelection selection) {
        RecipientDTO target = context.getMappedInstance( order, RecipientDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( order == null ) {
            return null;
        }

        RecipientDTO recipientDTO = new RecipientDTO();

        context.storeMappedInstance( order, recipientDTO );

        recipientDTO.setContact( contactMapper.contactToDto( order.getContact() ) );
        recipientDTO.setAddress( addressMapper.addressToDto( order.getAddress() ) );

        return recipientDTO;
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

    private ContactDTO requestRecipientContact(SubmitOrderRequest submitOrderRequest) {
        if ( submitOrderRequest == null ) {
            return null;
        }
        SubmitOrderRecipient recipient = submitOrderRequest.getRecipient();
        if ( recipient == null ) {
            return null;
        }
        ContactDTO contact = recipient.getContact();
        if ( contact == null ) {
            return null;
        }
        return contact;
    }

    private AddressDTO requestRecipientAddress(SubmitOrderRequest submitOrderRequest) {
        if ( submitOrderRequest == null ) {
            return null;
        }
        SubmitOrderRecipient recipient = submitOrderRequest.getRecipient();
        if ( recipient == null ) {
            return null;
        }
        AddressDTO address = recipient.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }
}
