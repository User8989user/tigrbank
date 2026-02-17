package com.tigrbank.importexport.yaml;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.importexport.DataExporter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.List;

public class YamlExporter implements DataExporter {
    private final Yaml yaml;

    public YamlExporter() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        yaml = new Yaml(options);
    }

    @Override
    public void exportData(List<BankAccount> accounts, List<Category> categories,
                           List<Operation> operations, String basePath) throws IOException {
        try (Writer writer = new FileWriter(basePath + "_accounts.yaml")) {
            yaml.dump(accounts, writer);
        }
        try (Writer writer = new FileWriter(basePath + "_categories.yaml")) {
            yaml.dump(categories, writer);
        }
        try (Writer writer = new FileWriter(basePath + "_operations.yaml")) {
            yaml.dump(operations, writer);
        }
    }
}