package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogItemDTO;
import me.kolek.ecommerce.dsgw.api.model.InventoryConnection;
import org.springframework.stereotype.Component;

@Component
public class CatalogItemResolver extends CatalogEntryResolver<CatalogItemDTO> {

}
