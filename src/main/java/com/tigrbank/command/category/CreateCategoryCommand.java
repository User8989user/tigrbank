package com.tigrbank.command.category;

import com.tigrbank.command.Command;
import com.tigrbank.domain.Type;
import com.tigrbank.facade.CategoryFacade;
import java.util.Scanner;

public class CreateCategoryCommand implements Command {
    private final CategoryFacade categoryFacade;
    private final Scanner scanner;

    public CreateCategoryCommand(CategoryFacade categoryFacade, Scanner scanner) {
        this.categoryFacade = categoryFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        System.out.print("Enter type (INCOME/EXPENSE): ");
        Type type = Type.valueOf(scanner.nextLine().toUpperCase());
        var category = categoryFacade.createCategory(type, name);
        System.out.println("Category created: " + category);
    }
}