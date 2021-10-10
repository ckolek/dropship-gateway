package me.kolek.ecommerce.dsgw.api.model.paging;

import java.util.List;
import lombok.Getter;

@Getter
public class Connection<N, E extends Edge<N>> {

  private final List<E> edges;
  private final PageInfo pageInfo;

  public Connection(List<E> edges, PageInfo pageInfo) {
    this.edges = edges;
    this.pageInfo = pageInfo;
  }
}
