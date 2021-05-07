package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface MappingFieldSelection {

  MappingFieldSelection ALL = new MappingFieldSelection() {
    @Override
    public Optional<SelectedField> getField(String name) {
      return Optional.of(new SelectedField() {
        @Override
        public String getName() {
          return name;
        }

        @Override
        public MappingFieldSelection getSelection() {
          return ALL;
        }
      });
    }
  };

  Optional<SelectedField> getField(String name);

  static Builder builder() {
    return new Builder();
  }

  interface SelectedField {
    String getName();

    MappingFieldSelection getSelection();
  }

  class Builder {

    private final Map<String, SelectedField> fields = new HashMap<>();

    public Builder field(String name) {
      return field(name, null);
    }

    public Builder field(String name, Consumer<Builder> consumer) {
      Builder fieldBuilder = new Builder();
      if (consumer != null) {
        consumer.accept(fieldBuilder);
      }
      MappingFieldSelection fieldSelection = fieldBuilder.build();
      fields.put(name, new SelectedField() {
        @Override
        public String getName() {
          return name;
        }

        @Override
        public MappingFieldSelection getSelection() {
          return fieldSelection;
        }
      });
      return this;
    }

    public MappingFieldSelection build() {
      return name -> Optional.ofNullable(fields.get(name));
    }
  }
}
