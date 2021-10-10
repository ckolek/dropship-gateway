package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import java.util.Set;
import me.kolek.ecommerce.dsgw.api.model.paging.CatalogConnection;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO;
import me.kolek.ecommerce.dsgw.api.model.paging.WarehouseConnection;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO.Status;
import org.springframework.stereotype.Component;

@Component
public class SupplierResolver implements GraphQLResolver<SupplierDTO> {

  public CatalogConnection catalogs(SupplierDTO supplierDTO, int pageSize, int pageOffset) {
    return null;
  }

  public WarehouseDTO warehouse(SupplierDTO supplierDTO, String supplierCode) {
    return null;
  }

  public WarehouseConnection warehouses(SupplierDTO supplierDTO, Set<Status> statuses, int pageSize,
      int pageOffset) {
    return null;
  }
}
