package com.tigrbank.importexport.csv;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.importexport.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CsvImporter extends DataImporterTemplate {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    protected RawData readRawData(String basePath) throws IOException {
        List<Map<String, String>> accountsRaw = readCsvToMaps(basePath + "_accounts.csv");
        List<Map<String, String>> categoriesRaw = readCsvToMaps(basePath + "_categories.csv");
        List<Map<String, String>> operationsRaw = readCsvToMaps(basePath + "_operations.csv");
        return new RawData(accountsRaw, categoriesRaw, operationsRaw);
    }

    @Override
    protected ParsedData parseRawData(RawData rawData) {
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();

        for (Map<String, String> row : rawData.getAccountsRaw()) {
            Long id = Long.parseLong(row.get("id"));
            String name = row.get("name");
            double balance = Double.parseDouble(row.get("balance"));
            accounts.add(new BankAccount(id, name, balance));
        }

        for (Map<String, String> row : rawData.getCategoriesRaw()) {
            Long id = Long.parseLong(row.get("id"));
            Type type = Type.valueOf(row.get("type"));
            String name = row.get("name");
            categories.add(new Category(id, type, name));
        }

        int lineNumber = 0;
        for (Map<String, String> row : rawData.getOperationsRaw()) {
            Long id = Long.parseLong(row.get("id"));
            Type type = Type.valueOf(row.get("type"));
            Long accountId = Long.parseLong(row.get("bankAccountId"));
            double amount = Double.parseDouble(row.get("amount"));

            String dateStr = row.get("date");
            LocalDate date;
            if (dateStr == null || dateStr.trim().isEmpty() || "00".equals(dateStr.trim())) {
                // Защита от некорректных данных: используем текущую дату
                date = LocalDate.now();
                System.err.println("WARNING: Invalid or missing date for operation " + id + " ('" + dateStr
                        + "'), using current date");
            } else {
                try {
                    date = LocalDate.parse(dateStr.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    // Если формат всё равно неверный — тоже fallback
                    date = LocalDate.now();
                    System.err.println(
                            "WARNING: Unparseable date '" + dateStr + "' for operation " + id + ", using current date");
                }
            }

            String description = row.get("description");
            Long categoryId = Long.parseLong(row.get("categoryId"));
            operations.add(new Operation(id, type, accountId, amount, date, description, categoryId));
        }

        return new ParsedData(accounts, categories, operations);
    }

    @Override
    protected ImportResult convertToDomain(ParsedData parsedData) {
        return new ImportResult(parsedData.getAccounts(), parsedData.getCategories(), parsedData.getOperations());
    }

    private List<Map<String, String>> readCsvToMaps(String filename) throws IOException {
        List<Map<String, String>> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String headerLine = reader.readLine();
            if (headerLine == null)
                return result;
            String[] headers = parseCsvLine(headerLine);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] values = parseCsvLine(line);
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    map.put(headers[i], unescapeCsv(values[i]));
                }
                result.add(map);
            }
        }
        return result;
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }

    private String unescapeCsv(String field) {
        if (field == null || field.isEmpty())
            return field;
        if (field.length() >= 2 && field.startsWith("\"") && field.endsWith("\"")) {
            String inner = field.substring(1, field.length() - 1);
            return inner.replace("\"\"", "\"");
        }
        return field;
    }
}