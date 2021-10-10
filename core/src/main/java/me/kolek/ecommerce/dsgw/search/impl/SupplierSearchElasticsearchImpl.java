package me.kolek.ecommerce.dsgw.search.impl;

import java.util.Optional;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.criteria.SupplierCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.SupplierDTO;
import me.kolek.ecommerce.dsgw.search.SupplierSearch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
public class SupplierSearchElasticsearchImpl
    extends BaseSearchElasticsearchImpl<SupplierCriteriaDTO, SupplierDTO> implements SupplierSearch {

  private static final IndexCoordinates INDEX_COORDINATES = IndexCoordinates.of("suppliers");

  @Inject
  public SupplierSearchElasticsearchImpl(ElasticsearchOperations elasticsearch) {
    super(elasticsearch, INDEX_COORDINATES, SupplierDTO.class);
  }

  @Override
  protected Optional<String> getId(SupplierCriteriaDTO criteria) {
    return Optional.ofNullable(criteria.getId());
  }

  @Override
  protected void buildQuery(BoolQueryBuilder queryBuilder, SupplierCriteriaDTO criteria) {
    QueryBuilding.buildQuery(queryBuilder.must(), "", criteria);
  }
}
