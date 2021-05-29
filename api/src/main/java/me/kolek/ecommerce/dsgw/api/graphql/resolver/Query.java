package me.kolek.ecommerce.dsgw.api.graphql.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Set;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.CarrierConnection;
import me.kolek.ecommerce.dsgw.api.model.CarrierDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogConnection;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryConnection;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.api.model.InventoryConnection;
import me.kolek.ecommerce.dsgw.api.model.InvoiceDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderConnection;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.api.model.PackageDTO;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelDTO;
import me.kolek.ecommerce.dsgw.api.model.SupplierConnection;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO;
import me.kolek.ecommerce.dsgw.api.model.WarehouseConnection;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO;
import me.kolek.ecommerce.dsgw.api.service.OrderService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class Query implements GraphQLQueryResolver {

  private final OrderService orderService;

  public CarrierDTO carrier(String id, String name) {
    return null;
  }

  public CarrierConnection carriers(int pageSize, int pageOffset) {
    return null;
  }

  public ServiceLevelDTO serviceLevel(String id, String code) {
    return null;
  }

  public CatalogDTO catalog(String id) {
    return null;
  }

  public CatalogConnection catalogs(int pageSize, int pageOffset) {
    return null;
  }

  public CatalogEntryDTO catalogEntry(String id) {
    return null;
  }

  public CatalogEntryConnection catalogEntries(String sku, String mpn, String gtin, String upc,
      String ean, String isbn, String manufacturer, String brand, int pageSize, int pageOffset) {
    return null;
  }

  public InventoryConnection inventory(String supplierId, String catalogId, String catalogEntryId,
      String warehouseId, int pageSize, int pageOffset) {
    return null;
  }

  public InvoiceDTO invoice(String id) {
    return null;
  }

  public OrderDTO order(String id, String orderNumber, DataFetchingEnvironment environment) {
    if (id != null) {
      return orderService.findOrderById(id, environment);
    } else if (orderNumber != null) {
      return orderService.findOrderByOrderNumber(orderNumber, environment);
    } else {
      throw new IllegalArgumentException("one of [id, orderNumber] is required");
    }
  }

  public OrderConnection orders(String customerOrderNumber, String supplierId, String warehouseId,
      Set<OrderDTO.Status> statuses, int pageSize, int pageOffset) {
    return null;
  }

  public PackageDTO shipment(String id) {
    return null;
  }

  public SupplierDTO supplier(String id, String name) {
    return null;
  }

  public SupplierConnection suppliers(Set<SupplierDTO.Status> statuses, int pageSize, int pageOffset) {
    return null;
  }

  public WarehouseDTO warehouse(String id, String code) {
    return null;
  }

  public WarehouseConnection warehouses(String supplierId, String supplierCode,
      Set<WarehouseDTO.Status> statuses, int pageSize, int pageOffset) {
    return null;
  }
}
