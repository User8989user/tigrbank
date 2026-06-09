package com.tigrbank.importexport;

import java.util.List;
import java.util.Map;

public class RawData {
    private final List<Map<String, String>> accountsRaw;
    private final List<Map<String, String>> categoriesRaw;
    private final List<Map<String, String>> operationsRaw;

    public RawData(List<Map<String, String>> accountsRaw,
                   List<Map<String, String>> categoriesRaw,
                   List<Map<String, String>> operationsRaw) {
        this.accountsRaw = accountsRaw;
        this.categoriesRaw = categoriesRaw;
        this.operationsRaw = operationsRaw;
    }

    public List<Map<String, String>> getAccountsRaw() { return accountsRaw; }
    public List<Map<String, String>> getCategoriesRaw() { return categoriesRaw; }
    public List<Map<String, String>> getOperationsRaw() { return operationsRaw; }
}