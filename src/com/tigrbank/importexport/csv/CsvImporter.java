package com.tigrbank.importexport.csv;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.ImportResult;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter implements DataImporter {
    @Override
    public ImportResult importData(String basePath) throws IOException {
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(basePath + "_accounts.csv"))) {
            String line = reader.readLine(); // заголовок
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length >= 3) {
                    Long id = Long.parseLong(parts[0]);
                    String name = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    accounts.add(new BankAccount(id, name, balance));
                }
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(basePath + "_categories.csv"))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length >= 3) {
                    Long id = Long.parseLong(parts[0]);
                    Type type = Type.valueOf(parts[1]);
                    String name = parts[2];
                    categories.add(new Category(id, type, name));
                }
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(basePath + "_operations.csv"))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length >= 7) {
                    Long id = Long.parseLong(parts[0]);
                    Type type = Type.valueOf(parts[1]);
                    Long accountId = Long.parseLong(parts[2]);
                    double amount = Double.parseDouble(parts[3]);
                    LocalDate date = LocalDate.parse(parts[4]);
                    String description = parts[5];
                    Long categoryId = Long.parseLong(parts[6]);
                    operations.add(new Operation(id, type, accountId, amount, date, description, categoryId));
                }
            }
        }

        return new ImportResult(accounts, categories, operations);
    }

    private String[] parseCsvLine(String line) {
        // Упрощённый парсер (не обрабатывает кавычки полностью)
        return line.split(",");
    }
}