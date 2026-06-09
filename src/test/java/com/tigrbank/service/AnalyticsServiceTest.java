package com.tigrbank.service;

import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {
    @Mock
    private OperationRepository operationRepo;
    @Mock
    private CategoryRepository categoryRepo;
    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getIncomeExpenseDifference_CalculatesCorrectly() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 31);
        List<Operation> ops = List.of(
                new Operation(1L, Type.INCOME, 1L, 100.0, LocalDate.of(2023, 1, 10), "", 1L),
                new Operation(2L, Type.EXPENSE, 1L, 30.0, LocalDate.of(2023, 1, 15), "", 2L),
                new Operation(3L, Type.INCOME, 1L, 50.0, LocalDate.of(2023, 1, 20), "", 1L)
        );
        when(operationRepo.findByDateBetween(start, end)).thenReturn(ops);

        double diff = analyticsService.getIncomeExpenseDifference(start, end);
        assertEquals(120.0, diff); // 100 -30 +50 = 120
    }

    @Test
    void getGroupedByCategory_ReturnsCorrectSums() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 31);
        Category catIncome = new Category(1L, Type.INCOME, "Salary");
        Category catExpense = new Category(2L, Type.EXPENSE, "Food");
        Category catOther = new Category(3L, Type.INCOME, "Other");

        List<Operation> ops = List.of(
                new Operation(1L, Type.INCOME, 1L, 100.0, LocalDate.of(2023, 1, 10), "", 1L),
                new Operation(2L, Type.EXPENSE, 1L, 30.0, LocalDate.of(2023, 1, 15), "", 2L),
                new Operation(3L, Type.INCOME, 1L, 50.0, LocalDate.of(2023, 1, 20), "", 1L),
                new Operation(4L, Type.EXPENSE, 1L, 20.0, LocalDate.of(2023, 1, 25), "", 2L)
        );
        when(operationRepo.findByDateBetween(start, end)).thenReturn(ops);
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(catIncome));
        when(categoryRepo.findById(2L)).thenReturn(Optional.of(catExpense));

        Map<Category, Double> grouped = analyticsService.getGroupedByCategory(start, end);

        assertEquals(2, grouped.size());
        assertEquals(150.0, grouped.get(catIncome)); // 100+50
        assertEquals(-50.0, grouped.get(catExpense)); // -30-20 = -50
    }

    @Test
    void getGroupedByCategory_WhenCategoryNotFound_ThrowsException() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        Operation op = new Operation(1L, Type.INCOME, 1L, 100.0, start, "", 999L);
        when(operationRepo.findByDateBetween(start, end)).thenReturn(List.of(op));
        when(categoryRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> analyticsService.getGroupedByCategory(start, end));
    }
}