package me.kolek.ecommerce.dsgw.api.util;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import me.kolek.ecommerce.dsgw.api.model.paging.Connection;
import me.kolek.ecommerce.dsgw.api.model.paging.Edge;
import me.kolek.ecommerce.dsgw.api.model.paging.PageInfo;
import org.springframework.data.domain.Page;

public class PageUtil {

  public static <N, E extends Edge<N>, C extends Connection<N, E>> C toConnection(Page<N> page,
      BiFunction<String, N, E> edgeConstructor,
      BiFunction<List<E>, PageInfo, C> connectionConstructor) {
    return toConnection(page, Function.identity(), edgeConstructor, connectionConstructor);
  }

  public static <T, N, E extends Edge<N>, C extends Connection<N, E>> C toConnection(Page<T> page,
      Function<T, N> mapper, BiFunction<String, N, E> edgeConstructor,
      BiFunction<List<E>, PageInfo, C> connectionConstructor) {
    var edges = page.get().map(mapper).map(node -> edgeConstructor.apply("", node)).toList();
    return connectionConstructor.apply(edges, toPageInfo(page));
  }

  public static PageInfo toPageInfo(Page<?> page) {
    return new PageInfo(page.getNumberOfElements(), page.getTotalElements(), page.getNumber(),
        page.getTotalPages());
  }
}
