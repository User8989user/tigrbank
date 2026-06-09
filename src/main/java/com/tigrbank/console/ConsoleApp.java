package com.tigrbank.console;

import com.tigrbank.command.CommandRegistry;
import com.tigrbank.di.DIContainer;
import com.tigrbank.service.StatisticsService;
import java.util.Scanner;

public class ConsoleApp {
    private final CommandRegistry commandRegistry;
    private final StatisticsService statisticsService;
    private final Scanner scanner;

    public ConsoleApp(DIContainer container) {
        this.commandRegistry = container.resolve(CommandRegistry.class);
        this.statisticsService = container.resolve(StatisticsService.class);
        this.scanner = container.resolve(Scanner.class);
    }

    public void start() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                commandRegistry.execute("0");
                break;
            }
            commandRegistry.execute(choice);
        }
        statisticsService.printStatistics();
    }

    private void printMainMenu() {
        System.out.println("\n=== TIGRBANK FINANCE MODULE ===");
        System.out.println("1. Create Account");
        System.out.println("2. List Accounts");
        System.out.println("3. Update Account");
        System.out.println("4. Delete Account");
        System.out.println("5. Recalculate Balance");
        System.out.println("6. Create Category");
        System.out.println("7. List Categories");
        System.out.println("8. Update Category");
        System.out.println("9. Delete Category");
        System.out.println("10. Create Operation");
        System.out.println("11. List Operations");
        System.out.println("12. Update Operation");
        System.out.println("13. Delete Operation");
        System.out.println("14. Show Analytics");
        System.out.println("15. Export Data");
        System.out.println("16. Import Data");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    public static void main(String[] args) {
        DIContainer container = AppConfig.createContainer();
        ConsoleApp app = new ConsoleApp(container);
        app.start();
    }
}