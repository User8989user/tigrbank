package com.tigrbank.service;

import com.tigrbank.importexport.DataExporter;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.ImportResult;
import com.tigrbank.repository.BankAccountRepository;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import java.io.IOException;
import java.util.Map;

public class ImportExportService {
    private final Map<String, DataExporter> exporters;
    private final Map<String, DataImporter> importers;
    private final BankAccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final OperationRepository operationRepo;

    public ImportExportService(Map<String, DataExporter> exporters,
                               Map<String, DataImporter> importers,
                               BankAccountRepository accountRepo,
                               CategoryRepository categoryRepo,
                               OperationRepository operationRepo) {
        this.exporters = exporters;
        this.importers = importers;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.operationRepo = operationRepo;
    }

    public void exportAll(String format, String basePath) throws IOException {
        DataExporter exporter = exporters.get(format.toUpperCase());
        if (exporter == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
        exporter.exportData(accountRepo.findAll(), categoryRepo.findAll(), operationRepo.findAll(), basePath);
        System.out.println("Data exported successfully to " + basePath + "_*." + format.toLowerCase());
    }

    public void importAll(String format, String basePath) throws IOException {
        DataImporter importer = importers.get(format.toUpperCase());
        if (importer == null) {
            throw new IllegalArgumentException("Unsupported import format: " + format);
        }
        ImportResult result = importer.importData(basePath);
        accountRepo.clear();
        categoryRepo.clear();
        operationRepo.clear();
        result.getAccounts().forEach(accountRepo::save);
        result.getCategories().forEach(categoryRepo::save);
        result.getOperations().forEach(operationRepo::save);
        System.out.println("Data imported successfully from " + basePath + "_*." + format.toLowerCase());
    }
}