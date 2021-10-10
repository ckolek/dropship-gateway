package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.InventoryDTO;

public class InventoryConnection extends Connection<InventoryDTO, InventoryEdge> {

  @JsonCreator
  public InventoryConnection(List<InventoryEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
