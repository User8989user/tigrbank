package com.tigrbank.console;

import com.tigrbank.command.*;
import com.tigrbank.command.account.*;
import com.tigrbank.command.category.*;
import com.tigrbank.command.operation.*;
import com.tigrbank.command.analytics.*;
import com.tigrbank.command.importexport.*;
import com.tigrbank.di.DIContainer;
import com.tigrbank.domain.factory.DomainObjectFactory;
import com.tigrbank.domain.factory.StandardDomainFactory;
import com.tigrbank.facade.*;
import com.tigrbank.importexport.*;
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
import java.util.Scanner;

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

        // Фабрика доменных объектов
        container.registerSingleton(DomainObjectFactory.class, new StandardDomainFactory());

        // Сервисы
        container.register(AccountService.class, () ->
                new AccountService(
                        container.resolve(BankAccountRepository.class),
                        container.resolve(OperationRepository.class),
                        container.resolve(DomainObjectFactory.class)
                ));
        container.register(CategoryService.class, () ->
                new CategoryService(
                        container.resolve(CategoryRepository.class),
                        container.resolve(OperationRepository.class),
                        container.resolve(DomainObjectFactory.class)
                ));
        container.register(OperationService.class, () ->
                new OperationService(
                        container.resolve(OperationRepository.class),
                        container.resolve(BankAccountRepository.class),
                        container.resolve(CategoryRepository.class),
                        container.resolve(DomainObjectFactory.class)
                ));
        container.register(AnalyticsService.class, () ->
                new AnalyticsService(
                        container.resolve(OperationRepository.class),
                        container.resolve(CategoryRepository.class)
                ));
        container.register(StatisticsService.class, StatisticsService::new);

        // Фасады
        container.register(AccountFacade.class, () ->
                new AccountFacade(container.resolve(AccountService.class)));
        container.register(CategoryFacade.class, () ->
                new CategoryFacade(container.resolve(CategoryService.class)));
        container.register(OperationFacade.class, () ->
                new OperationFacade(container.resolve(OperationService.class)));
        container.register(AnalyticsFacade.class, () ->
                new AnalyticsFacade(container.resolve(AnalyticsService.class)));

        // Экспортёры/импортёры
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

        // Сканер
        Scanner scanner = new Scanner(System.in);
        container.registerSingleton(Scanner.class, scanner);

        // Фасады для команд
        AccountFacade accountFacade = container.resolve(AccountFacade.class);
        CategoryFacade categoryFacade = container.resolve(CategoryFacade.class);
        OperationFacade operationFacade = container.resolve(OperationFacade.class);
        AnalyticsFacade analyticsFacade = container.resolve(AnalyticsFacade.class);
        ImportExportService importExportService = container.resolve(ImportExportService.class);
        StatisticsService statisticsService = container.resolve(StatisticsService.class);

        // Реестр команд
        CommandRegistry registry = new CommandRegistry();

        // Команды для счетов
        registry.register("1", new TimedCommand(new CreateAccountCommand(accountFacade, scanner), statisticsService));
        registry.register("2", new TimedCommand(new ListAccountsCommand(accountFacade), statisticsService));
        registry.register("3", new TimedCommand(new UpdateAccountCommand(accountFacade, scanner), statisticsService));
        registry.register("4", new TimedCommand(new DeleteAccountCommand(accountFacade, scanner), statisticsService));
        registry.register("5", new TimedCommand(new RecalculateBalanceCommand(accountFacade, scanner), statisticsService));

        // Команды для категорий
        registry.register("6", new TimedCommand(new CreateCategoryCommand(categoryFacade, scanner), statisticsService));
        registry.register("7", new TimedCommand(new ListCategoriesCommand(categoryFacade), statisticsService));
        registry.register("8", new TimedCommand(new UpdateCategoryCommand(categoryFacade, scanner), statisticsService));
        registry.register("9", new TimedCommand(new DeleteCategoryCommand(categoryFacade, scanner), statisticsService));

        // Команды для операций
        registry.register("10", new TimedCommand(new CreateOperationCommand(operationFacade, scanner), statisticsService));
        registry.register("11", new TimedCommand(new ListOperationsCommand(operationFacade), statisticsService));
        registry.register("12", new TimedCommand(new UpdateOperationCommand(operationFacade, scanner), statisticsService));
        registry.register("13", new TimedCommand(new DeleteOperationCommand(operationFacade, scanner), statisticsService));

        // Команда аналитики
        registry.register("14", new TimedCommand(new ShowAnalyticsCommand(analyticsFacade, scanner), statisticsService));

        // Команды импорта/экспорта
        registry.register("15", new TimedCommand(new ExportDataCommand(importExportService, scanner), statisticsService));
        registry.register("16", new TimedCommand(new ImportDataCommand(importExportService, scanner), statisticsService));

        // Выход
        registry.register("0", new ExitCommand());

        container.registerSingleton(CommandRegistry.class, registry);
        return container;
    }
}