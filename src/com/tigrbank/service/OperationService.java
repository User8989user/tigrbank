package com.tigrbank.service;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.repository.BankAccountRepository;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import java.time.LocalDate;
import java.util.List;

public class OperationService {
    private final OperationRepository operationRepo;
    private final BankAccountRepository accountRepo;
    private final CategoryRepository categoryRepo;

    public OperationService(OperationRepository operationRepo,
                            BankAccountRepository accountRepo,
                            CategoryRepository categoryRepo) {
        this.operationRepo = operationRepo;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
    }

    public Operation createOperation(Type type, Long accountId, double amount, LocalDate date,
                                     String description, Long categoryId) {
        BankAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (category.getType() != type) {
            throw new IllegalArgumentException("Category type does not match operation type");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (date == null) {
            date = LocalDate.now();
        }

        Operation op = new Operation(null, type, accountId, amount, date, description, categoryId);
        Operation saved = operationRepo.save(op);

        double delta = (type == Type.INCOME) ? amount : -amount;
        account.setBalance(account.getBalance() + delta);
        accountRepo.save(account);

        return saved;
    }

    public void deleteOperation(Long id) {
        Operation op = operationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
        BankAccount account = accountRepo.findById(op.getBankAccountId())
                .orElseThrow(() -> new IllegalStateException("Account for operation not found"));
        double delta = (op.getType() == Type.INCOME) ? -op.getAmount() : op.getAmount();
        account.setBalance(account.getBalance() + delta);
        accountRepo.save(account);

        operationRepo.delete(id);
    }

    public Operation updateDescription(Long id, String newDescription) {
        Operation op = operationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
        op.setDescription(newDescription);
        return operationRepo.save(op);
    }

    public List<Operation> getAllOperations() {
        return operationRepo.findAll();
    }

    public Operation getOperation(Long id) {
        return operationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
    }
}