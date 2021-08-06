package me.kolek.ecommerce.dsgw.api.model.graphql.paging;

import lombok.Getter;

@Getter
public class Edge<N> {

  private final String cursor;
  private final N node;

  public Edge(String cursor, N node) {
    this.cursor = cursor;
    this.node = node;
  }
}
