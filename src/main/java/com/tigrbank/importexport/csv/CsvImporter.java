package com.tigrbank.importexport.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.ImportResult;

public class CsvImporter implements DataImporter {
    @Override
    public ImportResult importData(String basePath) throws IOException {
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();

        accounts = readAccounts(basePath + "_accounts.csv");
        categories = readCategories(basePath + "_categories.csv");
        operations = readOperations(basePath + "_operations.csv");

        return new ImportResult(accounts, categories, operations);
    }

    private List<BankAccount> readAccounts(String filename) throws IOException {
        List<BankAccount> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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
        return accounts;
    }

    private List<Category> readCategories(String filename) throws IOException {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // заголовок
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
        return categories;
    }

    private List<Operation> readOperations(String filename) throws IOException {
        List<Operation> operations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // заголовок
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length >= 7) {
                    Long id = Long.parseLong(parts[0]);
                    Type type = Type.valueOf(parts[1]);
                    Long accountId = Long.parseLong(parts[2]);
                    double amount = Double.parseDouble(parts[3]);
                    LocalDate date = LocalDate.parse(parts[4]); // ожидается ISO-формат
                    String description = unescapeCsv(parts[5]);
                    Long categoryId = Long.parseLong(parts[6]);
                    operations.add(new Operation(id, type, accountId, amount, date, description, categoryId));
                }
            }
        }
        return operations;
    }

    /**
     * Парсит строку CSV с учётом кавычек.
     * Поддерживает поля, заключённые в двойные кавычки, и экранирование кавычек ("").
     */
    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // Встретили кавычку
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Экранированная кавычка внутри поля
                    sb.append('"');
                    i++; // пропускаем следующую кавычку
                } else {
                    // Переключение режима кавычек
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Конец поля
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString()); // последнее поле
        return result.toArray(new String[0]);
    }

    /**
     * Убирает внешние кавычки и восстанавливает экранированные кавычки ("" -> ").
     */
    private String unescapeCsv(String field) {
        if (field == null || field.isEmpty()) return field;
        if (field.length() >= 2 && field.startsWith("\"") && field.endsWith("\"")) {
            // Убираем внешние кавычки
            String inner = field.substring(1, field.length() - 1);
            // Заменяем "" на "
            return inner.replace("\"\"", "\"");
        }
        return field;
    }
}