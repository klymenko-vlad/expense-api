package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    void deleteCategory(String categoryId);
}
