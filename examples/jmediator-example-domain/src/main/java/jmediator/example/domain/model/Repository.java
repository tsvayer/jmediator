package jmediator.example.domain.model;

import java.io.Serializable;

public interface Repository<T extends Entity<ID>, ID extends Serializable> {
  <S extends T> S save(S entity);

  Iterable<T> findAll();

  T getOne(ID id);

  void delete(T entity);
}
