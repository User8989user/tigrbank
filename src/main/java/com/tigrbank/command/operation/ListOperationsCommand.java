package com.tigrbank.command.operation;

import com.tigrbank.command.Command;
import com.tigrbank.facade.OperationFacade;

public class ListOperationsCommand implements Command {
    private final OperationFacade operationFacade;

    public ListOperationsCommand(OperationFacade operationFacade) {
        this.operationFacade = operationFacade;
    }

    @Override
    public void execute() {
        var operations = operationFacade.getAllOperations();
        if (operations.isEmpty()) {
            System.out.println("No operations.");
        } else {
            operations.forEach(System.out::println);
        }
    }
}