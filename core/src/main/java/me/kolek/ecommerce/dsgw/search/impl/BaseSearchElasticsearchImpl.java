package me.kolek.ecommerce.dsgw.search.impl;

import static me.kolek.ecommerce.dsgw.search.impl.QueryBuilding.buildQuery;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.search.Search;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

@RequiredArgsConstructor
public abstract class BaseSearchElasticsearchImpl<C, R> implements Search<C, R> {
  private final ElasticsearchOperations elasticsearch;
  private final IndexCoordinates indexCoordinates;
  private final Class<R> resultClass;

  @Override
  public Page<R> search(C criteria, Pageable pageable) {
    Optional<String> id = getId(criteria);
    if (id.isPresent()) {
      R result = elasticsearch.get(id.get(), resultClass, indexCoordinates);
      return result != null ? new PageImpl<>(List.of(result)) : Page.empty();
    }

    var queryBuilder = QueryBuilders.boolQuery();
    buildQuery(queryBuilder, criteria);

    var query = new NativeSearchQueryBuilder()
        .withQuery(queryBuilder)
        .build();
    var hits = elasticsearch.search(query, resultClass, indexCoordinates);
    return new PageImpl<>(hits.stream().map(SearchHit::getContent).toList(), pageable,
        hits.getTotalHits());
  }

  protected abstract Optional<String> getId(C criteria);

  protected abstract void buildQuery(BoolQueryBuilder queryBuilder, C criteria);
}
