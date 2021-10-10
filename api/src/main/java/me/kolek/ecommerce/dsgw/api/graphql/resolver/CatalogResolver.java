package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.CatalogEntryConnection;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import org.springframework.stereotype.Component;

@Component
public class CatalogResolver implements GraphQLResolver<CatalogDTO> {

  public CatalogEntryDTO entry(CatalogDTO catalogDTO, String sku) {
    return null;
  }

  public CatalogEntryConnection entries(CatalogDTO catalogDTO, Boolean includeOptions, int pageSize,
      int pageLimit) {
    return null;
  }
}
