package com.tigrbank.repository.impl;

import com.tigrbank.domain.Category;
import com.tigrbank.domain.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCategoryRepositoryTest {
    private InMemoryCategoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCategoryRepository();
    }

    @Test
    void save_ShouldAssignId() {
        Category category = new Category(null, Type.INCOME, "Salary");
        Category saved = repository.save(category);
        assertNotNull(saved.getId());
        assertEquals(Type.INCOME, saved.getType());
        assertEquals("Salary", saved.getName());
    }

    @Test
    void findAll_ShouldReturnAll() {
        repository.save(new Category(null, Type.INCOME, "Salary"));
        repository.save(new Category(null, Type.EXPENSE, "Food"));
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void delete_ShouldRemove() {
        Category saved = repository.save(new Category(null, Type.INCOME, "Salary"));
        repository.delete(saved.getId());
        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void clear_ShouldEmpty() {
        repository.save(new Category(null, Type.INCOME, "Salary"));
        repository.clear();
        assertTrue(repository.findAll().isEmpty());
    }
}