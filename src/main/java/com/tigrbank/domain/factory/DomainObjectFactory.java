package com.tigrbank.domain.factory;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import java.time.LocalDate;

public interface DomainObjectFactory {
    BankAccount createBankAccount(String name, double initialBalance);
    Category createCategory(Type type, String name);
    Operation createOperation(Type type, Long accountId, double amount,
                              LocalDate date, String description, Long categoryId);
}

// Стандартная реализация
public class StandardDomainFactory implements DomainObjectFactory {
    @Override
    public BankAccount createBankAccount(String name, double initialBalance) {
        return new BankAccount(null, name, initialBalance);
    }

    @Override
    public Category createCategory(Type type, String name) {
        return new Category(null, type, name);
    }

    @Override
    public Operation createOperation(Type type, Long accountId, double amount,
                                     LocalDate date, String description, Long categoryId) {
        return new Operation(null, type, accountId, amount, date, description, categoryId);
    }
}