package com.tigrbank.service;

import com.tigrbank.domain.Category;
import com.tigrbank.domain.Type;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepo;
    private final OperationRepository operationRepo;

    public CategoryService(CategoryRepository categoryRepo, OperationRepository operationRepo) {
        this.categoryRepo = categoryRepo;
        this.operationRepo = operationRepo;
    }

    public Category createCategory(Type type, String name) {
        Category category = new Category(null, type, name);
        return categoryRepo.save(category);
    }

    public Category updateCategory(Long id, Type type, String name) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        category.setType(type);
        category.setName(name);
        return categoryRepo.save(category);
    }

    public void deleteCategory(Long id) {
        if (!operationRepo.findByCategoryId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing operations");
        }
        categoryRepo.delete(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }
}