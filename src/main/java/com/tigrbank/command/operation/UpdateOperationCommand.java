package com.tigrbank.command.operation;

import com.tigrbank.command.Command;
import com.tigrbank.facade.OperationFacade;
import java.util.Scanner;

public class UpdateOperationCommand implements Command {
    private final OperationFacade operationFacade;
    private final Scanner scanner;

    public UpdateOperationCommand(OperationFacade operationFacade, Scanner scanner) {
        this.operationFacade = operationFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter operation ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        operationFacade.updateOperationDescription(id, description);
        System.out.println("Operation updated.");
    }
}