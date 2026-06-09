package com.tigrbank.importexport.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.ImportResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlImporter implements DataImporter {
    private final ObjectMapper mapper;

    public YamlImporter() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ImportResult importData(String basePath) throws IOException {
        List<BankAccount> accounts = mapper.readValue(new File(basePath + "_accounts.yaml"),
                mapper.getTypeFactory().constructCollectionType(List.class, BankAccount.class));
        List<Category> categories = mapper.readValue(new File(basePath + "_categories.yaml"),
                mapper.getTypeFactory().constructCollectionType(List.class, Category.class));
        List<Operation> operations = mapper.readValue(new File(basePath + "_operations.yaml"),
                mapper.getTypeFactory().constructCollectionType(List.class, Operation.class));
        return new ImportResult(accounts, categories, operations);
    }
}