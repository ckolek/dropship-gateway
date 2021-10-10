package me.kolek.ecommerce.dsgw.api.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public abstract class EventDTO<T> {
  private Metadata metadata;
  private T record;

  @JsonIgnore
  public abstract String getTypeName();
  @JsonIgnore
  public abstract Optional<String> getSubTypeName();
}
