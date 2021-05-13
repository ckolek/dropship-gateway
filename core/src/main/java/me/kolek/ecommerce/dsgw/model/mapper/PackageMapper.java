package me.kolek.ecommerce.dsgw.model.mapper;

import static me.kolek.ecommerce.dsgw.model.mapper.MapperUtil.mapIfSelected;

import java.util.List;
import javax.inject.Inject;
import me.kolek.ecommerce.dsgw.api.model.PackageDTO;
import me.kolek.ecommerce.dsgw.api.model.PackageItemDTO;
import me.kolek.ecommerce.dsgw.model.Package;
import me.kolek.ecommerce.dsgw.model.PackageItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {OrderMapper.class,
    PackageItemMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class PackageMapper {

  public static final String FIELD__ORDER = "order";
  public static final String FIELD__ITEMS = "items";

  @Inject
  private OrderMapper orderMapper;

  @Mapping(target = FIELD__ORDER, ignore = true)
  @Mapping(target = FIELD__ITEMS, ignore = true)
  public abstract PackageDTO packageToDto(Package _package,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);

  @AfterMapping
  protected void afterMapping(Package _package, @MappingTarget PackageDTO packageDTO,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection) {
    mapIfSelected(selection, FIELD__ORDER, subSelection -> packageDTO
        .setOrder(orderMapper.orderToDto(_package.getOrder(), context, subSelection)));
    mapIfSelected(selection, FIELD__ITEMS, subSelection -> packageDTO
        .setItems(packageItemToDtoList(_package.getItems(), context, subSelection)));

    if (context.isSetParentReferences() && packageDTO.getItems() != null) {
      packageDTO.getItems().forEach(item -> item.setPackage(packageDTO));
    }
  }

  protected abstract List<PackageItemDTO> packageItemToDtoList(List<PackageItem> items,
      @Context CycleAvoidingMappingContext context, @Context MappingFieldSelection selection);
}
