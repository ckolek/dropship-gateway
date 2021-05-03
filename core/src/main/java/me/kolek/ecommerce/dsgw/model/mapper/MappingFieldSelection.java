package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.Map;
import java.util.Optional;

public interface MappingFieldSelection {
  Optional<SelectedField> getField(String name);

  interface SelectedField {
    String getName();

    MappingFieldSelection getSelection();
  }
}
