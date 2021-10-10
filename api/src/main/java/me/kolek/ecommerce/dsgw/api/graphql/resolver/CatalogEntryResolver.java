package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.InventoryConnection;
import org.springframework.stereotype.Component;

@Component
public class CatalogEntryResolver implements GraphQLResolver<CatalogEntryDTO> {

  public InventoryConnection inventory(CatalogEntryDTO catalogEntryDTO, String warehouseId,
      int pageSize, int pageOffset) {
    return null;
  }
}
