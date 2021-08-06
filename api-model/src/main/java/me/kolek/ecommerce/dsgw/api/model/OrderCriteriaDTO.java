package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private List<OrderDTO.Status> statuses;
  private List<OrderCancelCodeCriteriaDTO> cancelCodes;
  private String cancelReason;
  private DateTimeCriteriaDTO timeOrdered;
  private DateTimeCriteriaDTO timeReleased;
  private DateTimeCriteriaDTO timeAcknowledged;
  private DateTimeCriteriaDTO timeCancelled;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
