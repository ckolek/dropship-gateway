package me.kolek.ecommerce.dsgw.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Search<C, R> {
  Page<R> search(C criteria, Pageable pageable);
}
