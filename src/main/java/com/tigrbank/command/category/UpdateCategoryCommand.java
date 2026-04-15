package com.tigrbank.command.category;

import com.tigrbank.command.Command;
import com.tigrbank.domain.Type;
import com.tigrbank.facade.CategoryFacade;
import java.util.Scanner;

public class UpdateCategoryCommand implements Command {
    private final CategoryFacade categoryFacade;
    private final Scanner scanner;

    public UpdateCategoryCommand(CategoryFacade categoryFacade, Scanner scanner) {
        this.categoryFacade = categoryFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter category ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new type (INCOME/EXPENSE): ");
        Type type = Type.valueOf(scanner.nextLine().toUpperCase());
        categoryFacade.updateCategory(id, type, name);
        System.out.println("Category updated.");
    }
}