package com.tigrbank.importexport;

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