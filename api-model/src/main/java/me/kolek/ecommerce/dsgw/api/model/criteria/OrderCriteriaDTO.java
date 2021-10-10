package me.kolek.ecommerce.dsgw.api.model.criteria;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO.Status;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderCriteriaDTO {
  private String id;
  private String orderNumber;
  private String customerOrderNumber;
  private List<WarehouseCriteriaDTO> warehouses;
  private List<RecipientCriteriaDTO> recipients;
  private List<OrderItemCriteriaDTO> items;
  private List<ServiceLevelCriteriaDTO> serviceLevels;
  private List<Status> statuses;
  private List<OrderCancelCodeCriteriaDTO> cancelCodes;
  private String cancelReason;
  private DateTimeCriteriaDTO timeOrdered;
  private DateTimeCriteriaDTO timeReleased;
  private DateTimeCriteriaDTO timeAcknowledged;
  private DateTimeCriteriaDTO timeCancelled;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
