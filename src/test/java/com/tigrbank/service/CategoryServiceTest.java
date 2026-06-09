package com.tigrbank.service;

import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.domain.factory.DomainObjectFactory;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepo;
    @Mock
    private OperationRepository operationRepo;
    @InjectMocks
    private CategoryService categoryService;
    @Mock private DomainObjectFactory factory;

    @Test
void createCategory_ValidData_SavesAndReturns() {
    when(factory.createCategory(any(Type.class), anyString()))
        .thenAnswer(inv -> new Category(null, inv.getArgument(0), inv.getArgument(1)));
    when(categoryRepo.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

    Category category = categoryService.createCategory(Type.INCOME, "Salary");
    assertEquals(Type.INCOME, category.getType());
    assertEquals("Salary", category.getName());
    verify(factory).createCategory(Type.INCOME, "Salary");
}

    @Test
    void updateCategory_ExistingId_UpdatesFields() {
        Long id = 1L;
        Category existing = new Category(id, Type.INCOME, "Salary");
        when(categoryRepo.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Category updated = categoryService.updateCategory(id, Type.EXPENSE, "Food");
        assertEquals(Type.EXPENSE, updated.getType());
        assertEquals("Food", updated.getName());
        verify(categoryRepo).save(existing);
    }

    @Test
    void updateCategory_NonExisting_ThrowsException() {
        when(categoryRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(99L, Type.INCOME, "New"));
    }

    @Test
    void deleteCategory_WithNoOperations_Success() {
        Long id = 1L;
        when(operationRepo.findByCategoryId(id)).thenReturn(List.of());
        categoryService.deleteCategory(id);
        verify(categoryRepo).delete(id);
    }

    @Test
    void deleteCategory_WithOperations_ThrowsException() {
        Long id = 1L;
        when(operationRepo.findByCategoryId(id)).thenReturn(List.of(new Operation()));
        assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory(id));
    }

    @Test
    void getAllCategories_ReturnsList() {
        List<Category> expected = List.of(new Category(1L, Type.INCOME, "A"));
        when(categoryRepo.findAll()).thenReturn(expected);
        assertEquals(expected, categoryService.getAllCategories());
    }

    @Test
    void getCategory_ExistingId_ReturnsCategory() {
        Category cat = new Category(1L, Type.INCOME, "A");
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(cat));
        assertEquals(cat, categoryService.getCategory(1L));
    }

    @Test
    void getCategory_NonExisting_ThrowsException() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategory(1L));
    }
}