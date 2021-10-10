package me.kolek.ecommerce.dsgw.events;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.kolek.ecommerce.dsgw.api.model.event.Metadata;
import me.kolek.ecommerce.dsgw.api.model.event.CatalogEventDTO;
import me.kolek.ecommerce.dsgw.context.RequestContext;
import me.kolek.ecommerce.dsgw.events.config.EventProperties;
import me.kolek.ecommerce.dsgw.model.Catalog;
import me.kolek.ecommerce.dsgw.model.mapper.CatalogMapper;
import me.kolek.ecommerce.dsgw.model.mapper.MappingFieldSelection;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CatalogEventEmitter extends BaseEventEmitter {

  private final CatalogMapper catalogMapper;
  private final EventProperties eventProperties;

  private final MappingFieldSelection MAPPING_FIELD_SELECTION = MappingFieldSelection.builder()
      .field(CatalogMapper.FIELD__SUPPLIER)
      .build();

  @Inject
  public CatalogEventEmitter(ObjectMapper objectMapper,
      AmazonSNS sns,
      CatalogMapper catalogMapper,
      EventProperties eventProperties) {
    super(objectMapper, sns);
    this.catalogMapper = catalogMapper;
    this.eventProperties = eventProperties;
  }

  public void emitEvent(Catalog catalog, CatalogEventDTO.Type type) throws Exception {
    emitEvent(catalog, type, null);
  }

  public void emitEvent(Catalog catalog, CatalogEventDTO.Type type, CatalogEventDTO.SubType subType)
      throws Exception {
    var event = CatalogEventDTO.builder()
        .metadata(Metadata.builder()
            .requestId(RequestContext.get().getId())
            .recordId(catalog.getId().toString())
            .recordVersion(catalog.getRecordVersion().longValue())
            .timeEmitted(OffsetDateTime.now())
            .build())
        .type(type)
        .subType(subType)
        .record(catalogMapper.catalogToDto(catalog, false, MAPPING_FIELD_SELECTION))
        .build();

    emitEvent(event, eventProperties.getOrderEventTopicArn());
  }
}
