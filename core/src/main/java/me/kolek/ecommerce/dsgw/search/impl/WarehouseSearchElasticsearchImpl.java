package me.kolek.ecommerce.dsgw.search.impl;

import java.util.Optional;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.criteria.WarehouseCriteriaDTO;
import me.kolek.ecommerce.dsgw.api.model.WarehouseDTO;
import me.kolek.ecommerce.dsgw.search.WarehouseSearch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
public class WarehouseSearchElasticsearchImpl
    extends BaseSearchElasticsearchImpl<WarehouseCriteriaDTO, WarehouseDTO> implements WarehouseSearch {

  private static final IndexCoordinates INDEX_COORDINATES = IndexCoordinates.of("warehouses");

  @Inject
  public WarehouseSearchElasticsearchImpl(ElasticsearchOperations elasticsearch) {
    super(elasticsearch, INDEX_COORDINATES, WarehouseDTO.class);
  }

  @Override
  protected Optional<String> getId(WarehouseCriteriaDTO criteria) {
    return Optional.ofNullable(criteria.getId());
  }

  @Override
  protected void buildQuery(BoolQueryBuilder queryBuilder, WarehouseCriteriaDTO criteria) {
    QueryBuilding.buildQuery(queryBuilder.must(), "", criteria);
  }
}
