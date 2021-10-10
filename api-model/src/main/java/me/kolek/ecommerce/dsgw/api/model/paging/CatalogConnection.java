package me.kolek.ecommerce.dsgw.api.model.paging;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;

public class CatalogConnection extends Connection<CatalogDTO, CatalogEdge> {

  @JsonCreator
  public CatalogConnection(List<CatalogEdge> edges, PageInfo pageInfo) {
    super(edges, pageInfo);
  }
}
