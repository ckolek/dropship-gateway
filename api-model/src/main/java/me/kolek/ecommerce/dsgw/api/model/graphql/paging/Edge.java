package me.kolek.ecommerce.dsgw.api.model.graphql.paging;

import lombok.Getter;

@Getter
public class Edge<E> {

  private final String cursor;
  private final E node;

  public Edge(String cursor, E node) {
    this.cursor = cursor;
    this.node = node;
  }
}
