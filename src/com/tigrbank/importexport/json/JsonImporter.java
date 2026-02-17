package com.tigrbank.importexport.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.ImportResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonImporter implements DataImporter {
    private final ObjectMapper mapper;

    public JsonImporter() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ImportResult importData(String basePath) throws IOException {
        List<BankAccount> accounts = mapper.readValue(new File(basePath + "_accounts.json"),
                mapper.getTypeFactory().constructCollectionType(List.class, BankAccount.class));
        List<Category> categories = mapper.readValue(new File(basePath + "_categories.json"),
                mapper.getTypeFactory().constructCollectionType(List.class, Category.class));
        List<Operation> operations = mapper.readValue(new File(basePath + "_operations.json"),
                mapper.getTypeFactory().constructCollectionType(List.class, Operation.class));
        return new ImportResult(accounts, categories, operations);
    }
}