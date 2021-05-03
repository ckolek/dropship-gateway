package me.kolek.ecommerce.dsgw.api.graphql.service;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.model.mapper.MappingFieldSelection;

@RequiredArgsConstructor
public class DataFetchingEnvironmentMappingFieldSelection implements MappingFieldSelection {

  private final DataFetchingFieldSelectionSet selectionSet;

  private Map<String, SelectedField> selectedFields;

  @Override
  public Optional<SelectedField> getField(String name) {
    if (selectedFields == null) {
      selectedFields = selectionSet.getImmediateFields().stream().collect(Collectors
          .toMap(graphql.schema.SelectedField::getName, DataFetchingEnvironmentField::new,
              (f1, f2) -> f1));
    }
    return Optional.ofNullable(selectedFields.get(name));
  }

  public static DataFetchingEnvironmentMappingFieldSelection fromEnvironment(
      DataFetchingEnvironment environment) {
    return new DataFetchingEnvironmentMappingFieldSelection(environment.getSelectionSet());
  }

  @RequiredArgsConstructor
  private static class DataFetchingEnvironmentField implements SelectedField {
    private final graphql.schema.SelectedField selectedField;

    @Override
    public String getName() {
      return selectedField.getName();
    }

    @Override
    public MappingFieldSelection getSelection() {
      return new DataFetchingEnvironmentMappingFieldSelection(selectedField.getSelectionSet());
    }
  }
}
