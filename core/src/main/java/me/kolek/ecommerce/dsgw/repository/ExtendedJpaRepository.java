package me.kolek.ecommerce.dsgw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExtendedJpaRepository<T, ID> extends JpaRepository<T, ID> {

  void refresh(T entity);
}
