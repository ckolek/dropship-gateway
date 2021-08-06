package me.kolek.ecommerce.dsgw.test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import javax.inject.Inject;
import lombok.SneakyThrows;
import me.kolek.ecommerce.dsgw.model.mapper.AddressMapper;
import me.kolek.ecommerce.dsgw.model.mapper.AddressMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.CarrierMapper;
import me.kolek.ecommerce.dsgw.model.mapper.CarrierMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.CatalogEntryMapper;
import me.kolek.ecommerce.dsgw.model.mapper.CatalogEntryMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.CatalogMapper;
import me.kolek.ecommerce.dsgw.model.mapper.CatalogMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.ContactMapper;
import me.kolek.ecommerce.dsgw.model.mapper.ContactMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.InvoiceItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.InvoiceItemMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.InvoiceMapper;
import me.kolek.ecommerce.dsgw.model.mapper.InvoiceMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.ItemInventoryMapper;
import me.kolek.ecommerce.dsgw.model.mapper.ItemInventoryMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.OrderCancelCodeMapper;
import me.kolek.ecommerce.dsgw.model.mapper.OrderCancelCodeMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.OrderItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.OrderItemMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.OrderMapper;
import me.kolek.ecommerce.dsgw.model.mapper.OrderMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.PackageItemMapper;
import me.kolek.ecommerce.dsgw.model.mapper.PackageItemMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.PackageMapper;
import me.kolek.ecommerce.dsgw.model.mapper.PackageMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.ServiceLevelMapper;
import me.kolek.ecommerce.dsgw.model.mapper.ServiceLevelMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.SupplierMapper;
import me.kolek.ecommerce.dsgw.model.mapper.SupplierMapperImpl;
import me.kolek.ecommerce.dsgw.model.mapper.WarehouseMapper;
import me.kolek.ecommerce.dsgw.model.mapper.WarehouseMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class Mappers {

  private static final AddressMapper ADDRESS = new AddressMapperImpl();
  private static final CarrierMapper CARRIER = new CarrierMapperImpl();
  private static final CatalogMapper CATALOG = new CatalogMapperImpl();
  private static final CatalogEntryMapper CATALOG_ENTRY = new CatalogEntryMapperImpl();
  private static final ContactMapper CONTACT = new ContactMapperImpl();
  private static final InvoiceMapper INVOICE = new InvoiceMapperImpl();
  private static final InvoiceItemMapper INVOICE_ITEM = new InvoiceItemMapperImpl();
  private static final ItemInventoryMapper ITEM_INVENTORY = new ItemInventoryMapperImpl();
  private static final OrderMapper ORDER = new OrderMapperImpl();
  private static final OrderCancelCodeMapper ORDER_CANCEL_CODE = new OrderCancelCodeMapperImpl();
  private static final OrderItemMapper ORDER_ITEM = new OrderItemMapperImpl();
  private static final PackageMapper PACKAGE = new PackageMapperImpl();
  private static final PackageItemMapper PACKAGE_ITEM = new PackageItemMapperImpl();
  private static final ServiceLevelMapper SERVICE_LEVEL = new ServiceLevelMapperImpl();
  private static final SupplierMapper SUPPLIER = new SupplierMapperImpl();
  private static final WarehouseMapper WAREHOUSE = new WarehouseMapperImpl();

  static {
    var mappers = collectMappersByType();
    mappers.values().forEach(
        mapper -> collectInjectableFields(mapper).forEach(field -> inject(mapper, field, mappers)));
  }

  public static AddressMapper address() {
    return ADDRESS;
  }

  public static CarrierMapper carrier() {
    return CARRIER;
  }

  public static CatalogMapper catalog() {
    return CATALOG;
  }

  public static CatalogEntryMapper catalogEntry() {
    return CATALOG_ENTRY;
  }

  public static ContactMapper contact() {
    return CONTACT;
  }

  public static InvoiceMapper invoice() {
    return INVOICE;
  }

  public static InvoiceItemMapper invoiceItem() {
    return INVOICE_ITEM;
  }

  public static ItemInventoryMapper itemInventory() {
    return ITEM_INVENTORY;
  }

  public static OrderMapper order() {
    return ORDER;
  }

  public static OrderCancelCodeMapper orderCancelCode() {
    return ORDER_CANCEL_CODE;
  }

  public static OrderItemMapper orderItem() {
    return ORDER_ITEM;
  }

  public static PackageMapper _package() {
    return PACKAGE;
  }

  public static PackageItemMapper packageItem() {
    return PACKAGE_ITEM;
  }

  public static ServiceLevelMapper serviceLevel() {
    return SERVICE_LEVEL;
  }

  public static SupplierMapper supplier() {
    return SUPPLIER;
  }

  public static WarehouseMapper warehouse() {
    return WAREHOUSE;
  }

  @SneakyThrows
  private static Map<Class<?>, Object> collectMappersByType() {
    var fields = Mappers.class.getDeclaredFields();
    var mappers = new HashMap<Class<?>, Object>(fields.length);
    for (Field field : fields) {
      if (Modifier.isStatic(field.getModifiers())) {
        mappers.put(field.getType(), field.get(null));
      }
    }
    return mappers;
  }

  @SneakyThrows
  private static List<Field> collectInjectableFields(Object mapper) {
    return Stream.concat(Stream.of(mapper.getClass().getDeclaredFields()),
            Stream.of(mapper.getClass().getSuperclass().getDeclaredFields()))
        .filter(field -> field.isAnnotationPresent(Autowired.class)
            || field.isAnnotationPresent(Inject.class))
        .toList();
  }

  @SneakyThrows
  private static void inject(Object mapper, Field field, Map<Class<?>, Object> mappers) {
    var dependency = mappers.get(field.getType());
    if (dependency == null) {
      throw new NoSuchElementException("no dependency of type " + field.getType().getName());
    }

    try {
      field.setAccessible(true);
      field.set(mapper, dependency);
    } finally {
      field.setAccessible(false);
    }
  }
}
