package com.tigrbank.importexport;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import java.io.IOException;
import java.util.List;

public interface DataExporter {
    void exportData(List<BankAccount> accounts, List<Category> categories,
                    List<Operation> operations, String basePath) throws IOException;
}