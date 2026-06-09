package com.tigrbank.facade;

import com.tigrbank.domain.Category;
import com.tigrbank.service.AnalyticsService;
import java.time.LocalDate;
import java.util.Map;

public class AnalyticsFacade {
    private final AnalyticsService analyticsService;

    public AnalyticsFacade(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public double getIncomeExpenseDifference(LocalDate start, LocalDate end) {
        return analyticsService.getIncomeExpenseDifference(start, end);
    }

    public Map<Category, Double> getGroupedByCategory(LocalDate start, LocalDate end) {
        return analyticsService.getGroupedByCategory(start, end);
    }
}