package com.tigrbank.command.category;

import com.tigrbank.command.Command;
import com.tigrbank.facade.CategoryFacade;
import java.util.Scanner;

public class DeleteCategoryCommand implements Command {
    private final CategoryFacade categoryFacade;
    private final Scanner scanner;

    public DeleteCategoryCommand(CategoryFacade categoryFacade, Scanner scanner) {
        this.categoryFacade = categoryFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter category ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        categoryFacade.deleteCategory(id);
        System.out.println("Category deleted.");
    }
}