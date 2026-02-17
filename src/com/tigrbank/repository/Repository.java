package com.tigrbank.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void delete(Long id);
    boolean existsById(Long id);
    void clear();
}