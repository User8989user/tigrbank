package com.tigrbank.facade;

import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.service.OperationService;
import java.time.LocalDate;
import java.util.List;

public class OperationFacade {
    private final OperationService operationService;

    public OperationFacade(OperationService operationService) {
        this.operationService = operationService;
    }

    public Operation createOperation(Type type, Long accountId, double amount,
                                     LocalDate date, String description, Long categoryId) {
        return operationService.createOperation(type, accountId, amount, date, description, categoryId);
    }

    public void deleteOperation(Long id) {
        operationService.deleteOperation(id);
    }

    public void updateOperationDescription(Long id, String newDescription) {
        operationService.updateDescription(id, newDescription);
    }

    public List<Operation> getAllOperations() {
        return operationService.getAllOperations();
    }

    public Operation getOperation(Long id) {
        return operationService.getOperation(id);
    }
}