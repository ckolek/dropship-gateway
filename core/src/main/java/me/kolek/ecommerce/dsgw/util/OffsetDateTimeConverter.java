package me.kolek.ecommerce.dsgw.util;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(OffsetDateTime attribute) {
    return new Timestamp(attribute.toInstant().toEpochMilli());
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(Timestamp dbData) {
    return OffsetDateTime.ofInstant(dbData.toInstant(), ZoneId.systemDefault());
  }
}
