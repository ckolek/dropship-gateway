package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.api.model.InventoryConnection;

public class CatalogEntryResolver<CE extends CatalogEntryDTO> implements GraphQLResolver<CE> {

  public InventoryConnection inventory(CE catalogEntryDTO, String warehouseId, int pageSize,
      int pageOffset) {
    return null;
  }
}
