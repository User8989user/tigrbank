package com.tigrbank.command.category;

import com.tigrbank.command.Command;
import com.tigrbank.facade.CategoryFacade;

public class ListCategoriesCommand implements Command {
    private final CategoryFacade categoryFacade;

    public ListCategoriesCommand(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    @Override
    public void execute() {
        var categories = categoryFacade.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories.");
        } else {
            categories.forEach(System.out::println);
        }
    }
}