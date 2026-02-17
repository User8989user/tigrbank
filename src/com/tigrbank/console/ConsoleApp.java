package com.tigrbank.console;

import com.tigrbank.di.DIContainer;
import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.service.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final DIContainer container;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final OperationService operationService;
    private final AnalyticsService analyticsService;
    private final ImportExportService importExportService;
    private final StatisticsService statisticsService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(DIContainer container) {
        this.container = container;
        this.accountService = container.resolve(AccountService.class);
        this.categoryService = container.resolve(CategoryService.class);
        this.operationService = container.resolve(OperationService.class);
        this.analyticsService = container.resolve(AnalyticsService.class);
        this.importExportService = container.resolve(ImportExportService.class);
        this.statisticsService = container.resolve(StatisticsService.class);
    }

    public void start() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> measure("Account Management", this::accountMenu);
                case "2" -> measure("Category Management", this::categoryMenu);
                case "3" -> measure("Operation Management", this::operationMenu);
                case "4" -> measure("Analytics", this::analyticsMenu);
                case "5" -> measure("Import/Export", this::importExportMenu);
                case "6" -> statisticsService.printStatistics();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void measure(String scenario, Runnable action) {
        long start = System.currentTimeMillis();
        action.run();
        long duration = System.currentTimeMillis() - start;
        statisticsService.record(scenario, duration);
        System.out.printf("Scenario '%s' took %d ms%n", scenario, duration);
    }

    private void printMainMenu() {
        System.out.println("\n=== TIGRBANK FINANCE MODULE ===");
        System.out.println("1. Account Management");
        System.out.println("2. Category Management");
        System.out.println("3. Operation Management");
        System.out.println("4. Analytics");
        System.out.println("5. Import/Export");
        System.out.println("6. Show Statistics");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private void accountMenu() {
        while (true) {
            System.out.println("\n--- Account Management ---");
            System.out.println("1. Create Account");
            System.out.println("2. List Accounts");
            System.out.println("3. Update Account Name");
            System.out.println("4. Delete Account");
            System.out.println("5. Recalculate Balance");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createAccount();
                case "2" -> listAccounts();
                case "3" -> updateAccount();
                case "4" -> deleteAccount();
                case "5" -> recalculateBalance();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter account name: ");
        String name = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double balance = Double.parseDouble(scanner.nextLine());
        try {
            BankAccount account = accountService.createAccount(name, balance);
            System.out.println("Account created: " + account);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listAccounts() {
        List<BankAccount> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts.");
        } else {
            accounts.forEach(System.out::println);
        }
    }

    private void updateAccount() {
        System.out.print("Enter account ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        try {
            BankAccount updated = accountService.updateAccountName(id, newName);
            System.out.println("Account updated: " + updated);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteAccount() {
        System.out.print("Enter account ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            accountService.deleteAccount(id);
            System.out.println("Account deleted.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void recalculateBalance() {
        System.out.print("Enter account ID to recalculate balance: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            accountService.recalculateBalance(id);
            System.out.println("Balance recalculated.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void categoryMenu() {
        while (true) {
            System.out.println("\n--- Category Management ---");
            System.out.println("1. Create Category");
            System.out.println("2. List Categories");
            System.out.println("3. Update Category");
            System.out.println("4. Delete Category");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createCategory();
                case "2" -> listCategories();
                case "3" -> updateCategory();
                case "4" -> deleteCategory();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createCategory() {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        System.out.print("Enter type (INCOME/EXPENSE): ");
        String typeStr = scanner.nextLine().toUpperCase();
        try {
            Type type = Type.valueOf(typeStr);
            Category cat = categoryService.createCategory(type, name);
            System.out.println("Category created: " + cat);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories.");
        } else {
            categories.forEach(System.out::println);
        }
    }

    private void updateCategory() {
        System.out.print("Enter category ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new type (INCOME/EXPENSE): ");
        String typeStr = scanner.nextLine().toUpperCase();
        try {
            Type type = Type.valueOf(typeStr);
            Category updated = categoryService.updateCategory(id, type, name);
            System.out.println("Category updated: " + updated);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteCategory() {
        System.out.print("Enter category ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            categoryService.deleteCategory(id);
            System.out.println("Category deleted.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void operationMenu() {
        while (true) {
            System.out.println("\n--- Operation Management ---");
            System.out.println("1. Create Operation");
            System.out.println("2. List Operations");
            System.out.println("3. Update Operation Description");
            System.out.println("4. Delete Operation");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createOperation();
                case "2" -> listOperations();
                case "3" -> updateOperation();
                case "4" -> deleteOperation();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createOperation() {
        try {
            System.out.print("Enter type (INCOME/EXPENSE): ");
            Type type = Type.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Enter account ID: ");
            Long accountId = Long.parseLong(scanner.nextLine());
            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter date (yyyy-mm-dd) or leave empty for today: ");
            String dateStr = scanner.nextLine();
            LocalDate date = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr);
            System.out.print("Enter description (optional): ");
            String description = scanner.nextLine();
            System.out.print("Enter category ID: ");
            Long categoryId = Long.parseLong(scanner.nextLine());

            Operation op = operationService.createOperation(type, accountId, amount, date, description, categoryId);
            System.out.println("Operation created: " + op);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listOperations() {
        List<Operation> ops = operationService.getAllOperations();
        if (ops.isEmpty()) {
            System.out.println("No operations.");
        } else {
            ops.forEach(System.out::println);
        }
    }

    private void updateOperation() {
        System.out.print("Enter operation ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        try {
            Operation op = operationService.updateDescription(id, description);
            System.out.println("Operation updated: " + op);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteOperation() {
        System.out.print("Enter operation ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            operationService.deleteOperation(id);
            System.out.println("Operation deleted.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void analyticsMenu() {
        System.out.println("\n--- Analytics ---");
        System.out.print("Enter start date (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter end date (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());

        double diff = analyticsService.getIncomeExpenseDifference(start, end);
        System.out.printf("Income - Expense for period: %.2f%n", diff);

        var grouped = analyticsService.getGroupedByCategory(start, end);
        System.out.println("Grouped by category:");
        grouped.forEach((cat, sum) -> System.out.printf("  %s: %.2f%n", cat.getName(), sum));
    }

    private void importExportMenu() {
        System.out.println("\n--- Import/Export ---");
        System.out.println("1. Export data");
        System.out.println("2. Import data");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> exportData();
            case "2" -> importData();
            case "0" -> {}
            default -> System.out.println("Invalid choice.");
        }
    }

    private void exportData() {
        System.out.print("Enter format (CSV, JSON, YAML): ");
        String format = scanner.nextLine().toUpperCase();
        System.out.print("Enter base path for files (e.g., ./export/data): ");
        String path = scanner.nextLine();
        try {
            importExportService.exportAll(format, path);
        } catch (IOException e) {
            System.out.println("Export failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void importData() {
        System.out.print("Enter format (CSV, JSON, YAML): ");
        String format = scanner.nextLine().toUpperCase();
        System.out.print("Enter base path for files (e.g., ./export/data): ");
        String path = scanner.nextLine();
        try {
            importExportService.importAll(format, path);
        } catch (IOException e) {
            System.out.println("Import failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DIContainer container = AppConfig.createContainer();
        ConsoleApp app = new ConsoleApp(container);
        app.start();
    }
}