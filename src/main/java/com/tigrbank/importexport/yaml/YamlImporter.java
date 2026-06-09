package com.tigrbank.importexport.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class YamlImporter extends DataImporterTemplate {
    private final ObjectMapper mapper;

    public YamlImporter() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected RawData readRawData(String basePath) throws IOException {
        // Читаем YAML-файлы в сырые списки Map
        List<Map<String, Object>> accountsRaw = mapper.readValue(
                new File(basePath + "_accounts.yaml"),
                mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
        List<Map<String, Object>> categoriesRaw = mapper.readValue(
                new File(basePath + "_categories.yaml"),
                mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
        List<Map<String, Object>> operationsRaw = mapper.readValue(
                new File(basePath + "_operations.yaml"),
                mapper.getTypeFactory().constructCollectionType(List.class, Map.class));

        return new RawData(
                convertToStringMaps(accountsRaw),
                convertToStringMaps(categoriesRaw),
                convertToStringMaps(operationsRaw));
    }

    @Override
    protected ParsedData parseRawData(RawData rawData) {
        List<BankAccount> accounts = rawData.getAccountsRaw().stream()
                .map(row -> new BankAccount(
                        Long.valueOf((String) row.get("id")),
                        (String) row.get("name"),
                        Double.parseDouble((String) row.get("balance"))
                )).toList();

        List<Category> categories = rawData.getCategoriesRaw().stream()
                .map(row -> new Category(
                        Long.valueOf((String) row.get("id")),
                        com.tigrbank.domain.Type.valueOf((String) row.get("type")),
                        (String) row.get("name")
                )).toList();

        List<Operation> operations = rawData.getOperationsRaw().stream()
                .map(row -> new Operation(
                        Long.valueOf((String) row.get("id")),
                        com.tigrbank.domain.Type.valueOf((String) row.get("type")),
                        Long.valueOf((String) row.get("bankAccountId")),
                        Double.parseDouble((String) row.get("amount")),
                        java.time.LocalDate.parse((String) row.get("date")),
                        (String) row.get("description"),
                        Long.valueOf((String) row.get("categoryId"))
                )).toList();

        return new ParsedData(accounts, categories, operations);
    }

    @Override
    protected ImportResult convertToDomain(ParsedData parsedData) {
        return new ImportResult(parsedData.getAccounts(), parsedData.getCategories(), parsedData.getOperations());
    }

    private List<Map<String, String>> convertToStringMaps(List<Map<String, Object>> raw) {
        return raw.stream()
                .map(map -> {
                    Map<String, String> stringMap = new java.util.LinkedHashMap<>();
                    map.forEach((k, v) -> stringMap.put(k, v == null ? "" : v.toString()));
                    return stringMap;
                })
                .toList();
    }
}