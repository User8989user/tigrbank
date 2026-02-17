package com.tigrbank.repository.impl;

import com.tigrbank.domain.Operation;
import com.tigrbank.repository.OperationRepository;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryOperationRepository implements OperationRepository {
    private final Map<Long, Operation> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Operation save(Operation entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.getAndIncrement());
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Operation> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Operation> findAll() {
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

    @Override
    public List<Operation> findByBankAccountId(Long accountId) {
        return storage.values().stream()
                .filter(op -> op.getBankAccountId().equals(accountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByCategoryId(Long categoryId) {
        return storage.values().stream()
                .filter(op -> op.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByDateBetween(LocalDate start, LocalDate end) {
        return storage.values().stream()
                .filter(op -> !op.getDate().isBefore(start) && !op.getDate().isAfter(end))
                .collect(Collectors.toList());
    }
}