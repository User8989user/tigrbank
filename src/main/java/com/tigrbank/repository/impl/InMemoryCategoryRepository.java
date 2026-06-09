package com.tigrbank.repository.impl;

import com.tigrbank.domain.Category;
import com.tigrbank.repository.CategoryRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Category save(Category entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.getAndIncrement());
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public void clear() {
        storage.clear();
        idGenerator.set(1);
    }
}