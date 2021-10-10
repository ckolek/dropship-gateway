package me.kolek.ecommerce.dsgw.search.impl;

import java.util.Optional;
import me.kolek.ecommerce.dsgw.api.model.criteria.CatalogCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;
import me.kolek.ecommerce.dsgw.search.CatalogSearch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
public class CatalogSearchElasticsearchImpl
    extends BaseSearchElasticsearchImpl<CatalogCriteriaDTO, CatalogDTO> implements CatalogSearch {

  private static final IndexCoordinates INDEX_COORDINATES = IndexCoordinates.of("catalogs");

  public CatalogSearchElasticsearchImpl(ElasticsearchOperations elasticsearch) {
    super(elasticsearch, INDEX_COORDINATES, CatalogDTO.class);
  }

  @Override
  protected Optional<String> getId(CatalogCriteriaDTO criteria) {
    return Optional.ofNullable(criteria.getId());
  }

  @Override
  protected void buildQuery(BoolQueryBuilder queryBuilder, CatalogCriteriaDTO criteria) {
    QueryBuilding.buildQuery(queryBuilder.must(), "", criteria);
  }
}
