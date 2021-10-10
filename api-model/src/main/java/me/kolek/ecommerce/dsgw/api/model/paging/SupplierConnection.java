package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO;

public class SupplierConnection extends Connection<SupplierDTO, SupplierEdge> {

  @JsonCreator
  public SupplierConnection(List<SupplierEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
