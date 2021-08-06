package me.kolek.ecommerce.dsgw.api.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.IntCriteria;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItemCriteriaDTO {
  private String id;
  private IntCriteria lineNumber;
  private List<CatalogEntryCriteriaDTO> catalogEntries;
  private IntCriteria quantity;
  private String customization;
  private DateTimeCriteriaDTO expectedShipDate;
  private DateTimeCriteriaDTO expectedDeliveryDate;
  private List<OrderDTO.Status> statuses;
  private IntCriteria quantityAccepted;
  private IntCriteria quantityRejected;
  private List<OrderCancelCodeCriteriaDTO> rejectCodes;
  private String rejectReason;
  private DateTimeCriteriaDTO timeAcknowledged;
  private IntCriteria quantityCancelled;
  private List<OrderCancelCodeCriteriaDTO> cancelCodes;
  private String cancelReason;
  private DateTimeCriteriaDTO timeCancelled;
  private DateTimeCriteriaDTO timeCreated;
  private DateTimeCriteriaDTO timeUpdated;
}
