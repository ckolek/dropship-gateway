package me.kolek.ecommerce.dsgw.search.impl;

import static me.kolek.ecommerce.dsgw.search.impl.QueryBuilding.buildQuery;

import java.util.List;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.OrderCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.OrderDTO;
import me.kolek.ecommerce.dsgw.search.OrderSearch;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class OrderSearchElasticsearchImpl implements OrderSearch {

  private static final IndexCoordinates INDEX_COORDINATES = IndexCoordinates.of("orders");

  private final ElasticsearchOperations elasticsearch;

  @Override
  public Page<OrderDTO> searchOrders(OrderCriteriaDTO criteria, Pageable pageable) {
    if (criteria.getId() != null) {
      OrderDTO result = elasticsearch.get(criteria.getId(), OrderDTO.class, INDEX_COORDINATES);
      return result != null ? new PageImpl<>(List.of(result)) : Page.empty();
    }

    var queryBuilder = QueryBuilders.boolQuery();
    buildQuery(queryBuilder.must(), "", criteria);
    
    var query = new NativeSearchQueryBuilder()
        .withQuery(queryBuilder)
        .build();
    var hits = elasticsearch.search(query, OrderDTO.class, INDEX_COORDINATES);
    return new PageImpl<>(hits.stream().map(SearchHit::getContent).toList(), pageable,
        hits.getTotalHits());
  }
}
