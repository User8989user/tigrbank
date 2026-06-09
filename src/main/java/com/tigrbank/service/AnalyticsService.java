package com.tigrbank.service;

import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsService {
    private final OperationRepository operationRepo;
    private final CategoryRepository categoryRepo;

    public AnalyticsService(OperationRepository operationRepo, CategoryRepository categoryRepo) {
        this.operationRepo = operationRepo;
        this.categoryRepo = categoryRepo;
    }

    public double getIncomeExpenseDifference(LocalDate start, LocalDate end) {
        List<Operation> ops = operationRepo.findByDateBetween(start, end);
        return ops.stream()
                .mapToDouble(op -> op.getType() == Type.INCOME ? op.getAmount() : -op.getAmount())
                .sum();
    }

    public Map<Category, Double> getGroupedByCategory(LocalDate start, LocalDate end) {
        List<Operation> ops = operationRepo.findByDateBetween(start, end);
        Map<Long, Double> sumsByCategoryId = new HashMap<>();
        for (Operation op : ops) {
            double amount = op.getType() == Type.INCOME ? op.getAmount() : -op.getAmount();
            sumsByCategoryId.merge(op.getCategoryId(), amount, Double::sum);
        }

        Map<Category, Double> result = new HashMap<>();
        for (Map.Entry<Long, Double> entry : sumsByCategoryId.entrySet()) {
            Category category = categoryRepo.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalStateException("Category not found for operation"));
            result.put(category, entry.getValue());
        }
        return result;
    }
}