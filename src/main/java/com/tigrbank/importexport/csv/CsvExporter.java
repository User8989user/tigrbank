package com.tigrbank.importexport.csv;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.DataExporter;
import java.io.*;
import java.util.List;

public class CsvExporter implements DataExporter {
    @Override
    public void exportData(List<BankAccount> accounts, List<Category> categories,
            List<Operation> operations, String basePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(basePath + "_accounts.csv"))) {
            writer.println("id,name,balance");
            for (BankAccount a : accounts) {
                writer.printf("%d,%s,%.2f%n", a.getId(), a.getName(), a.getBalance());
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(basePath + "_categories.csv"))) {
            writer.println("id,type,name");
            for (Category c : categories) {
                writer.printf("%d,%s,%s%n", c.getId(), c.getType(), c.getName());
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(basePath + "_operations.csv"))) {
            writer.println("id,type,bankAccountId,amount,date,description,categoryId");
            for (Operation o : operations) {
                String dateStr = o.getDate() != null ? o.getDate().toString() : "";
                writer.printf("%d,%s,%d,%.2f,%s,%s,%d%n",
                        o.getId(), o.getType(), o.getBankAccountId(), o.getAmount(),
                        escapeCsv(dateStr),
                        escapeCsv(o.getDescription()),
                        o.getCategoryId());
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}