package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.function.Consumer;
import me.kolek.ecommerce.dsgw.model.mapper.MappingFieldSelection.SelectedField;

public class MapperUtil {

  public static void mapIfSelected(MappingFieldSelection selection, String fieldName,
      Consumer<MappingFieldSelection> fieldSelectionConsumer) {
    selection.getField(fieldName).map(SelectedField::getSelection)
        .ifPresent(fieldSelectionConsumer);
  }
}
