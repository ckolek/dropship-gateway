package me.kolek.ecommerce.dsgw.search.impl;

import java.util.Optional;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.criteria.OrderCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.search.OrderSearch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
public class OrderSearchElasticsearchImpl
    extends BaseSearchElasticsearchImpl<OrderCriteriaDTO, OrderDTO> implements OrderSearch {

  private static final IndexCoordinates INDEX_COORDINATES = IndexCoordinates.of("orders");

  @Inject
  public OrderSearchElasticsearchImpl(ElasticsearchOperations elasticsearch) {
    super(elasticsearch, INDEX_COORDINATES, OrderDTO.class);
  }

  @Override
  protected Optional<String> getId(OrderCriteriaDTO criteria) {
    return Optional.ofNullable(criteria.getId());
  }

  @Override
  protected void buildQuery(BoolQueryBuilder queryBuilder, OrderCriteriaDTO criteria) {
    QueryBuilding.buildQuery(queryBuilder.must(), "", criteria);
  }
}
