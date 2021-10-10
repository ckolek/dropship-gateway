package me.kolek.ecommerce.dsgw.api.model.event;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.kolek.ecommerce.dsgw.api.model.CatalogDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class CatalogEventDTO extends EventDTO<CatalogDTO> {
  private Type type;
  private SubType subType;

  @Override
  public String getTypeName() {
    return type.name();
  }

  @Override
  public Optional<String> getSubTypeName() {
    return Optional.ofNullable(subType).map(Enum::name);
  }

  public enum Type {
    CATALOG_CREATED,
    CATALOG_UPDATED,
    CATALOG_DELETED
  }

  public enum SubType {
  }
}
