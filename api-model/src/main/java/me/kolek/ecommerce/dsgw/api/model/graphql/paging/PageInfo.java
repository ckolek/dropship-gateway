package me.kolek.ecommerce.dsgw.api.model.graphql.paging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PageInfo {

  private final int count;
  private final long totalCount;
  private final int pageNumber;
  private final int totalPages;
}
