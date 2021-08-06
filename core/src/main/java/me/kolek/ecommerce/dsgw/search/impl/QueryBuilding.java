package me.kolek.ecommerce.dsgw.search.impl;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.AddressCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.CarrierCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.ContactCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.DateTimeCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderCancelCodeCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderItemCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.RecipientCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.ServiceLevelCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.SupplierCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.WarehouseCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.FloatCriteria;
import me.kolek.ecommerce.dsgw.api.model.graphql.paging.IntCriteria;
import org.elasticsearch.common.TriConsumer;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueryBuilding {

  public static BoolQueryBuilder must(Consumer<List<QueryBuilder>> build) {
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    build.accept(query.must());
    return query;
  }

  public static BoolQueryBuilder should(Consumer<List<QueryBuilder>> build) {
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    build.accept(query.should());
    return query;
  }

  public static <T> void multi(List<QueryBuilder> queries, String prefix, Iterable<T> elements,
      TriConsumer<List<QueryBuilder>, String, T> build) {
    if (elements != null) {
      Iterator<T> iterator = elements.iterator();
      if (iterator.hasNext()) {
        T first = iterator.next();

        if (iterator.hasNext()) {
          queries.add(should(q -> {
            q.add(must(q2 -> build.apply(q2, prefix, first)));
            while (iterator.hasNext()) {
              q.add(must(q2 -> build.apply(q2, prefix, iterator.next())));
            }
          }));
        } else {
          build.apply(queries, prefix, first);
        }
      }
    }
  }

  public static void match(List<QueryBuilder> queries, String fieldName, Object value) {
    if (value != null) {
      queries.add(QueryBuilders.matchQuery(fieldName, value));
    }
  }

  public static void in(List<QueryBuilder> queries, String fieldName, Iterable<?> values) {
    multi(queries, fieldName, values, QueryBuilding::match);
  }

  public static void bounded(List<QueryBuilder> queries, String fieldName, Object lowerBound,
      Object upperBound) {
    if (lowerBound != null || upperBound != null) {
      queries.add(QueryBuilders.rangeQuery(fieldName).from(lowerBound, true).to(upperBound, false));
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      OrderCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "orderNumber", criteria.getOrderNumber());
      match(queries, prefix + "customerOrderNumber", criteria.getCustomerOrderNumber());
      multi(queries, prefix + "warehouse.", criteria.getWarehouses(), QueryBuilding::buildQuery);
      multi(queries, prefix + "recipient.", criteria.getRecipients(), QueryBuilding::buildQuery);
      multi(queries, prefix + "items.", criteria.getItems(), QueryBuilding::buildQuery);
      multi(queries, prefix + "serviceLevel.", criteria.getServiceLevels(),
          QueryBuilding::buildQuery);
      in(queries, prefix + "status", criteria.getStatuses());
      multi(queries, prefix + "cancelCode.", criteria.getCancelCodes(), QueryBuilding::buildQuery);
      match(queries, prefix + "cancelReason", criteria.getCancelReason());
      buildQuery(queries, prefix + "timeOrdered", criteria.getTimeOrdered());
      buildQuery(queries, prefix + "timeReleased", criteria.getTimeReleased());
      buildQuery(queries, prefix + "timeAcknowledged", criteria.getTimeAcknowledged());
      buildQuery(queries, prefix + "timeCancelled", criteria.getTimeCancelled());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      OrderItemCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "id", criteria.getId());
      buildQuery(queries, prefix + "lineNumber", criteria.getLineNumber());
      multi(queries, prefix + "catalogEntry.", criteria.getCatalogEntries(),
          QueryBuilding::buildQuery);
      buildQuery(queries, prefix + "quantity", criteria.getQuantity());
      match(queries, prefix + "customization", criteria.getCustomization());
      buildQuery(queries, prefix + "expectedShipDate", criteria.getExpectedShipDate());
      buildQuery(queries, prefix + "expectedDeliveryDate", criteria.getExpectedDeliveryDate());
      in(queries, prefix + "status", criteria.getStatuses());
      buildQuery(queries, prefix + "quantityAccepted", criteria.getQuantityAccepted());
      buildQuery(queries, prefix + "quantityRejected", criteria.getQuantityRejected());
      multi(queries, prefix + "rejectCode.", criteria.getRejectCodes(), QueryBuilding::buildQuery);
      match(queries, prefix + "rejectReason", criteria.getRejectReason());
      buildQuery(queries, prefix + "timeAcknowledged", criteria.getTimeAcknowledged());
      buildQuery(queries, prefix + "quantityCancelled", criteria.getQuantityCancelled());
      multi(queries, prefix + "cancelCode.", criteria.getCancelCodes(), QueryBuilding::buildQuery);
      match(queries, prefix + "cancelReason", criteria.getCancelReason());
      buildQuery(queries, prefix + "timeCancelled", criteria.getTimeCancelled());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      SupplierCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "id", criteria.getId());
      match(queries, prefix + "name", criteria.getName());
      in(queries, prefix + "status", criteria.getStatuses());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      WarehouseCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "id", criteria.getId());
      multi(queries, prefix + "supplier.", criteria.getSuppliers(), QueryBuilding::buildQuery);
      match(queries, prefix + "code", criteria.getCode());
      match(queries, prefix + "supplierCode", criteria.getSupplierCode());
      match(queries, prefix + "description", criteria.getDescription());
      in(queries, prefix + "status", criteria.getStatuses());
      buildQuery(queries, prefix + "address.", criteria.getAddress());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      RecipientCriteriaDTO criteria) {
    if (criteria != null) {
      buildQuery(queries, prefix + "contact.", criteria.getContact());
      buildQuery(queries, prefix + "address.", criteria.getAddress());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      ContactCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "name", criteria.getName());
      match(queries, prefix + "phone", criteria.getPhone());
      match(queries, prefix + "email", criteria.getEmail());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      AddressCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "line1", criteria.getLine1());
      match(queries, prefix + "line2", criteria.getLine2());
      match(queries, prefix + "line3", criteria.getLine3());
      match(queries, prefix + "city", criteria.getCity());
      match(queries, prefix + "state", criteria.getState());
      match(queries, prefix + "province", criteria.getProvince());
      match(queries, prefix + "postalCode", criteria.getPostalCode());
      match(queries, prefix + "country", criteria.getCountry());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      CarrierCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "id", criteria.getId());
      match(queries, prefix + "name", criteria.getName());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      ServiceLevelCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "id", criteria.getId());
      multi(queries, prefix + "carrier.", criteria.getCarriers(), QueryBuilding::buildQuery);
      match(queries, prefix + "mode", criteria.getMode());
      match(queries, prefix + "code", criteria.getCode());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      OrderCancelCodeCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "id", criteria.getId());
      match(queries, prefix + "code", criteria.getCode());
      match(queries, prefix + "description", criteria.getDescription());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      CatalogCriteriaDTO criteria) {
    if (criteria != null) {
      match(queries, prefix + "id", criteria.getId());
      match(queries, prefix + "externalId", criteria.getExternalId());
      multi(queries, prefix + "supplier.", criteria.getSuppliers(), QueryBuilding::buildQuery);
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String prefix,
      CatalogEntryCriteriaDTO criteria) {
    if (criteria != null) {
      if (Boolean.TRUE.equals(criteria.getItem())) {
        prefix += "item.";
      }

      match(queries, prefix + "id", criteria.getId());
      multi(queries, prefix + "catalog.", criteria.getCatalogs(), QueryBuilding::buildQuery);
      match(queries, prefix + "name", criteria.getName());
      match(queries, prefix + "shortDescription", criteria.getShortDescription());
      match(queries, prefix + "longDescription", criteria.getLongDescription());
      match(queries, prefix + "sku", criteria.getSku());
      match(queries, prefix + "mpn", criteria.getMpn());
      match(queries, prefix + "gtin", criteria.getGtin());
      match(queries, prefix + "upc", criteria.getUpc());
      match(queries, prefix + "ean", criteria.getEan());
      match(queries, prefix + "isbn", criteria.getIsbn());
      match(queries, prefix + "manufacturer", criteria.getManufacturer());
      match(queries, prefix + "brand", criteria.getBrand());
      buildQuery(queries, prefix + "timeCreated", criteria.getTimeCreated());
      buildQuery(queries, prefix + "timeUpdated", criteria.getTimeUpdated());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String fieldName,
      DateTimeCriteriaDTO criteria) {
    if (criteria != null) {
      bounded(queries, fieldName, criteria.getAfter(), criteria.getBefore());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String fieldName,
      IntCriteria criteria) {
    if (criteria != null) {
      bounded(queries, fieldName, criteria.getMin(), criteria.getMax());
    }
  }

  public static void buildQuery(List<QueryBuilder> queries, String fieldName,
      FloatCriteria criteria) {
    if (criteria != null) {
      bounded(queries, fieldName, criteria.getMin(), criteria.getMax());
    }
  }
}
