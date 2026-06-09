package com.tigrbank.service;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.importexport.DataExporter;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.ImportResult;
import com.tigrbank.importexport.csv.CsvExporter;
import com.tigrbank.importexport.csv.CsvImporter;
import com.tigrbank.importexport.json.JsonExporter;
import com.tigrbank.importexport.json.JsonImporter;
import com.tigrbank.importexport.yaml.YamlExporter;
import com.tigrbank.importexport.yaml.YamlImporter;
import com.tigrbank.repository.BankAccountRepository;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import com.tigrbank.repository.impl.InMemoryBankAccountRepository;
import com.tigrbank.repository.impl.InMemoryCategoryRepository;
import com.tigrbank.repository.impl.InMemoryOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ImportExportServiceTest {
    private ImportExportService importExportService;
    private BankAccountRepository accountRepo;
    private CategoryRepository categoryRepo;
    private OperationRepository operationRepo;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        accountRepo = new InMemoryBankAccountRepository();
        categoryRepo = new InMemoryCategoryRepository();
        operationRepo = new InMemoryOperationRepository();

        Map<String, DataExporter> exporters = Map.of(
                "CSV", new CsvExporter(),
                "JSON", new JsonExporter(),
                "YAML", new YamlExporter()
        );
        Map<String, DataImporter> importers = Map.of(
                "CSV", new CsvImporter(),
                "JSON", new JsonImporter(),
                "YAML", new YamlImporter()
        );

        importExportService = new ImportExportService(exporters, importers, accountRepo, categoryRepo, operationRepo);
    }

    private void populateData() {
        accountRepo.save(new BankAccount(1L, "Main", 1000.0));
        accountRepo.save(new BankAccount(2L, "Savings", 500.0));
        categoryRepo.save(new Category(1L, Type.INCOME, "Salary"));
        categoryRepo.save(new Category(2L, Type.EXPENSE, "Food"));
        operationRepo.save(new Operation(1L, Type.INCOME, 1L, 200.0, LocalDate.of(2023, 1, 10), "Jan salary", 1L));
        operationRepo.save(new Operation(2L, Type.EXPENSE, 1L, 50.0, LocalDate.of(2023, 1, 15), "Lunch", 2L));
    }

    @Test
    void exportAndImportCsv_RoundTrip_Success() throws IOException {
        populateData();
        String basePath = tempDir.resolve("export").toString();

        // Экспорт
        importExportService.exportAll("CSV", basePath);

        // Очищаем репозитории
        accountRepo.clear();
        categoryRepo.clear();
        operationRepo.clear();
        assertTrue(accountRepo.findAll().isEmpty());

        // Импорт
        importExportService.importAll("CSV", basePath);

        // Проверяем, что данные восстановлены
        assertEquals(2, accountRepo.findAll().size());
        assertEquals(2, categoryRepo.findAll().size());
        assertEquals(2, operationRepo.findAll().size());
    }

    @Test
    void exportAndImportJson_RoundTrip_Success() throws IOException {
        populateData();
        String basePath = tempDir.resolve("export").toString();

        importExportService.exportAll("JSON", basePath);
        accountRepo.clear();
        categoryRepo.clear();
        operationRepo.clear();
        importExportService.importAll("JSON", basePath);

        assertEquals(2, accountRepo.findAll().size());
        assertEquals(2, categoryRepo.findAll().size());
        assertEquals(2, operationRepo.findAll().size());
    }

    @Test
    void exportAndImportYaml_RoundTrip_Success() throws IOException {
        populateData();
        String basePath = tempDir.resolve("export").toString();

        importExportService.exportAll("YAML", basePath);
        accountRepo.clear();
        categoryRepo.clear();
        operationRepo.clear();
        importExportService.importAll("YAML", basePath);

        assertEquals(2, accountRepo.findAll().size());
        assertEquals(2, categoryRepo.findAll().size());
        assertEquals(2, operationRepo.findAll().size());
    }

    @Test
    void exportWithUnsupportedFormat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> importExportService.exportAll("XML", "dummy"));
    }

    @Test
    void importWithUnsupportedFormat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> importExportService.importAll("XML", "dummy"));
    }
}