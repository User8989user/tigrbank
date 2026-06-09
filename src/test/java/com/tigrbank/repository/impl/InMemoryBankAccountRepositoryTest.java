package com.tigrbank.repository.impl;

import com.tigrbank.domain.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBankAccountRepositoryTest {
    private InMemoryBankAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBankAccountRepository();
    }

    @Test
    void save_ShouldAssignIdAndStore() {
        BankAccount account = new BankAccount(null, "Test", 100.0);
        BankAccount saved = repository.save(account);
        assertNotNull(saved.getId());
        assertEquals("Test", saved.getName());
        assertEquals(100.0, saved.getBalance());
    }

    @Test
    void findById_ShouldReturnAccountIfExists() {
        BankAccount saved = repository.save(new BankAccount(null, "Test", 100.0));
        Optional<BankAccount> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void findById_ShouldReturnEmptyIfNotExists() {
        Optional<BankAccount> found = repository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllAccounts() {
        repository.save(new BankAccount(null, "A", 10));
        repository.save(new BankAccount(null, "B", 20));
        List<BankAccount> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void delete_ShouldRemoveAccount() {
        BankAccount saved = repository.save(new BankAccount(null, "Test", 100.0));
        repository.delete(saved.getId());
        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void existsById_ShouldReturnTrueIfExists() {
        BankAccount saved = repository.save(new BankAccount(null, "Test", 100.0));
        assertTrue(repository.existsById(saved.getId()));
    }

    @Test
    void clear_ShouldRemoveAll() {
        repository.save(new BankAccount(null, "A", 10));
        repository.save(new BankAccount(null, "B", 20));
        repository.clear();
        assertTrue(repository.findAll().isEmpty());
    }
}