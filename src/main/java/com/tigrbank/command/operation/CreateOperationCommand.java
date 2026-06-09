package com.tigrbank.command.operation;

import com.tigrbank.command.Command;
import com.tigrbank.domain.Type;
import com.tigrbank.facade.OperationFacade;
import java.time.LocalDate;
import java.util.Scanner;

public class CreateOperationCommand implements Command {
    private final OperationFacade operationFacade;
    private final Scanner scanner;

    public CreateOperationCommand(OperationFacade operationFacade, Scanner scanner) {
        this.operationFacade = operationFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
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

            var operation = operationFacade.createOperation(type, accountId, amount, date, description, categoryId);
            System.out.println("Operation created: " + operation);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}