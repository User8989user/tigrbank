package com.tigrbank.importexport;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import java.util.List;

public class ParsedData {
    private final List<BankAccount> accounts;
    private final List<Category> categories;
    private final List<Operation> operations;

    public ParsedData(List<BankAccount> accounts, List<Category> categories, List<Operation> operations) {
        this.accounts = accounts;
        this.categories = categories;
        this.operations = operations;
    }

    public List<BankAccount> getAccounts() { return accounts; }
    public List<Category> getCategories() { return categories; }
    public List<Operation> getOperations() { return operations; }
}