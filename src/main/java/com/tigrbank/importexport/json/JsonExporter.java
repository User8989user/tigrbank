package com.tigrbank.importexport.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.DataExporter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonExporter implements DataExporter {
    private final ObjectMapper mapper;

    public JsonExporter() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void exportData(List<BankAccount> accounts, List<Category> categories,
                           List<Operation> operations, String basePath) throws IOException {
        mapper.writeValue(new File(basePath + "_accounts.json"), accounts);
        mapper.writeValue(new File(basePath + "_categories.json"), categories);
        mapper.writeValue(new File(basePath + "_operations.json"), operations);
    }
}