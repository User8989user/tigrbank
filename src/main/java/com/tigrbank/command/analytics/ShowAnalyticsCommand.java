package com.tigrbank.command.analytics;

import com.tigrbank.command.Command;
import com.tigrbank.facade.AnalyticsFacade;
import java.time.LocalDate;
import java.util.Scanner;

public class ShowAnalyticsCommand implements Command {
    private final AnalyticsFacade analyticsFacade;
    private final Scanner scanner;

    public ShowAnalyticsCommand(AnalyticsFacade analyticsFacade, Scanner scanner) {
        this.analyticsFacade = analyticsFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter start date (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter end date (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());

        double diff = analyticsFacade.getIncomeExpenseDifference(start, end);
        System.out.printf("Income - Expense for period: %.2f%n", diff);

        var grouped = analyticsFacade.getGroupedByCategory(start, end);
        System.out.println("Grouped by category:");
        grouped.forEach((cat, sum) -> System.out.printf("  %s: %.2f%n", cat.getName(), sum));
    }
}