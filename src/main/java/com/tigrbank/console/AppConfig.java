package com.tigrbank.console;

import com.tigrbank.di.DIContainer;
import com.tigrbank.importexport.DataExporter;
import com.tigrbank.importexport.DataImporter;
import com.tigrbank.importexport.csv.CsvExporter;
import com.tigrbank.importexport.csv.CsvImporter;
import com.tigrbank.importexport.json.JsonExporter;
import com.tigrbank.importexport.json.JsonImporter;
import com.tigrbank.importexport.yaml.YamlExporter;
import com.tigrbank.importexport.yaml.YamlImporter;
import com.tigrbank.repository.*;
import com.tigrbank.repository.impl.*;
import com.tigrbank.service.*;
import java.util.Map;

public class AppConfig {
    public static DIContainer createContainer() {
        DIContainer container = new DIContainer();

        // Репозитории (синглтоны)
        BankAccountRepository accountRepo = new InMemoryBankAccountRepository();
        CategoryRepository categoryRepo = new InMemoryCategoryRepository();
        OperationRepository operationRepo = new InMemoryOperationRepository();

        container.registerSingleton(BankAccountRepository.class, accountRepo);
        container.registerSingleton(CategoryRepository.class, categoryRepo);
        container.registerSingleton(OperationRepository.class, operationRepo);

        // Сервисы
        container.register(AccountService.class, () ->
                new AccountService(container.resolve(BankAccountRepository.class),
                        container.resolve(OperationRepository.class)));
        container.register(CategoryService.class, () ->
                new CategoryService(container.resolve(CategoryRepository.class),
                        container.resolve(OperationRepository.class)));
        container.register(OperationService.class, () ->
                new OperationService(container.resolve(OperationRepository.class),
                        container.resolve(BankAccountRepository.class),
                        container.resolve(CategoryRepository.class)));
        container.register(AnalyticsService.class, () ->
                new AnalyticsService(container.resolve(OperationRepository.class),
                        container.resolve(CategoryRepository.class)));
        container.register(StatisticsService.class, StatisticsService::new);

        // Экспортёры и импортёры
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

        container.register(ImportExportService.class, () ->
                new ImportExportService(exporters, importers,
                        container.resolve(BankAccountRepository.class),
                        container.resolve(CategoryRepository.class),
                        container.resolve(OperationRepository.class)));

        return container;
    }
}