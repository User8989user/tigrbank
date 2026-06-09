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

