package com.tigrbank.repository.impl;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.repository.BankAccountRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryBankAccountRepository implements BankAccountRepository {
    private final Map<Long, BankAccount> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public BankAccount save(BankAccount entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.getAndIncrement());
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
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