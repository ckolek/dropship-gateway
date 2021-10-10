package me.kolek.ecommerce.dsgw.search.impl;

import java.util.Optional;
import me.kolek.ecommerce.dsgw.api.model.criteria.CatalogEntryCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogEntryDTO;
import me.kolek.ecommerce.dsgw.search.CatalogEntrySearch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
public class CatalogEntrySearchElasticsearchImpl
    extends BaseSearchElasticsearchImpl<CatalogEntryCriteriaDTO, CatalogEntryDTO>
    implements CatalogEntrySearch {

  private static final IndexCoordinates INDEX_COORDINATES = IndexCoordinates.of("catalog_entries");

  public CatalogEntrySearchElasticsearchImpl(ElasticsearchOperations elasticsearch) {
    super(elasticsearch, INDEX_COORDINATES, CatalogEntryDTO.class);
  }

  @Override
  protected Optional<String> getId(CatalogEntryCriteriaDTO criteria) {
    return Optional.ofNullable(criteria.getId());
  }

  @Override
  protected void buildQuery(BoolQueryBuilder queryBuilder, CatalogEntryCriteriaDTO criteria) {
    QueryBuilding.buildQuery(queryBuilder.must(), "", criteria);
  }
}
