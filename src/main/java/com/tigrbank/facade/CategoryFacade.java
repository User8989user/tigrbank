package com.tigrbank.facade;

import com.tigrbank.domain.Category;
import com.tigrbank.domain.Type;
import com.tigrbank.service.CategoryService;
import java.util.List;

public class CategoryFacade {
    private final CategoryService categoryService;

    public CategoryFacade(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Category createCategory(Type type, String name) {
        return categoryService.createCategory(type, name);
    }

    public void updateCategory(Long id, Type type, String name) {
        categoryService.updateCategory(id, type, name);
    }

    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public Category getCategory(Long id) {
        return categoryService.getCategory(id);
    }
}