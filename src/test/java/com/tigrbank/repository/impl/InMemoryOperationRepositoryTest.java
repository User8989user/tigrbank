package com.tigrbank.repository.impl;

import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOperationRepositoryTest {
    private InMemoryOperationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOperationRepository();
    }

    @Test
    void save_ShouldAssignId() {
        Operation op = new Operation(null, Type.INCOME, 1L, 100.0, LocalDate.now(), "desc", 1L);
        Operation saved = repository.save(op);
        assertNotNull(saved.getId());
    }

    @Test
    void findByBankAccountId_ShouldReturnOperationsForAccount() {
        Operation op1 = repository.save(new Operation(null, Type.INCOME, 1L, 100.0, LocalDate.now(), "desc", 1L));
        Operation op2 = repository.save(new Operation(null, Type.INCOME, 2L, 200.0, LocalDate.now(), "desc", 1L));
        Operation op3 = repository.save(new Operation(null, Type.EXPENSE, 1L, 50.0, LocalDate.now(), "desc", 2L));

        List<Operation> forAccount1 = repository.findByBankAccountId(1L);
        assertEquals(2, forAccount1.size());
        assertTrue(forAccount1.contains(op1));
        assertTrue(forAccount1.contains(op3));
    }

    @Test
    void findByCategoryId_ShouldReturnOperationsForCategory() {
        Operation op1 = repository.save(new Operation(null, Type.INCOME, 1L, 100.0, LocalDate.now(), "desc", 1L));
        Operation op2 = repository.save(new Operation(null, Type.EXPENSE, 2L, 50.0, LocalDate.now(), "desc", 1L));
        Operation op3 = repository.save(new Operation(null, Type.EXPENSE, 3L, 30.0, LocalDate.now(), "desc", 2L));

        List<Operation> forCategory1 = repository.findByCategoryId(1L);
        assertEquals(2, forCategory1.size());
        assertTrue(forCategory1.contains(op1));
        assertTrue(forCategory1.contains(op2));
    }

    @Test
    void findByDateBetween_ShouldReturnOperationsInRange() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);

        Operation opToday = repository.save(new Operation(null, Type.INCOME, 1L, 100.0, today, "desc", 1L));
        Operation opYesterday = repository.save(new Operation(null, Type.INCOME, 1L, 100.0, yesterday, "desc", 1L));
        Operation opTomorrow = repository.save(new Operation(null, Type.INCOME, 1L, 100.0, tomorrow, "desc", 1L));

        List<Operation> between = repository.findByDateBetween(yesterday, today);
        assertEquals(2, between.size());
        assertTrue(between.contains(opYesterday));
        assertTrue(between.contains(opToday));
        assertFalse(between.contains(opTomorrow));
    }
}