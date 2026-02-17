package com.tigrbank.importexport.yaml;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.ImportResult;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.*;
import java.util.List;

public class YamlImporter implements DataImporter {
    private final Yaml yaml;

    public YamlImporter() {
        yaml = new Yaml(new Constructor(List.class));
    }

    @Override
    public ImportResult importData(String basePath) throws IOException {
        List<BankAccount> accounts;
        List<Category> categories;
        List<Operation> operations;

        try (Reader reader = new FileReader(basePath + "_accounts.yaml")) {
            accounts = yaml.load(reader);
        }
        try (Reader reader = new FileReader(basePath + "_categories.yaml")) {
            categories = yaml.load(reader);
        }
        try (Reader reader = new FileReader(basePath + "_operations.yaml")) {
            operations = yaml.load(reader);
        }

        return new ImportResult(accounts, categories, operations);
    }
}