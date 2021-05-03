package me.kolek.ecommerce.dsgw.repository.impl;

import javax.persistence.EntityManager;
import me.kolek.ecommerce.dsgw.repository.ExtendedJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class ExtendedJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements
    ExtendedJpaRepository<T, ID> {

  private final EntityManager entityManager;

  @Autowired
  public ExtendedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public void refresh(T entity) {
    entityManager.refresh(entity);
  }
}
