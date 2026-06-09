package com.tigrbank.importexport.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.DataExporter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlExporter implements DataExporter {
    private final ObjectMapper mapper;

    public YamlExporter() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void exportData(List<BankAccount> accounts, List<Category> categories,
                           List<Operation> operations, String basePath) throws IOException {
        mapper.writeValue(new File(basePath + "_accounts.yaml"), accounts);
        mapper.writeValue(new File(basePath + "_categories.yaml"), categories);
        mapper.writeValue(new File(basePath + "_operations.yaml"), operations);
    }
}