package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public class UuidMapper {
  public String uuidToString(UUID uuid) {
    if (uuid == null) {
      return null;
    }
    return uuid.toString();
  }
}
