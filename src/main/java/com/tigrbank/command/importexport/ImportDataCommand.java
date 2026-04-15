package com.tigrbank.command.importexport;

import com.tigrbank.command.Command;
import com.tigrbank.service.ImportExportService;
import java.util.Scanner;

public class ImportDataCommand implements Command {
    private final ImportExportService importExportService;
    private final Scanner scanner;

    public ImportDataCommand(ImportExportService importExportService, Scanner scanner) {
        this.importExportService = importExportService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter format (CSV, JSON, YAML): ");
        String format = scanner.nextLine().toUpperCase();
        System.out.print("Enter base path: ");
        String path = scanner.nextLine();
        try {
            importExportService.importAll(format, path);
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }
}