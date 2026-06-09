package com.tigrbank.command.operation;

import com.tigrbank.command.Command;
import com.tigrbank.facade.OperationFacade;
import java.util.Scanner;

public class DeleteOperationCommand implements Command {
    private final OperationFacade operationFacade;
    private final Scanner scanner;

    public DeleteOperationCommand(OperationFacade operationFacade, Scanner scanner) {
        this.operationFacade = operationFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter operation ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        operationFacade.deleteOperation(id);
        System.out.println("Operation deleted.");
    }
}