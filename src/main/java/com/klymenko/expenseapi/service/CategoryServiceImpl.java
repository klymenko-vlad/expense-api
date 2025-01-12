package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.dto.CategoryDTO;
import com.klymenko.expenseapi.entity.CategoryEntity;
import com.klymenko.expenseapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.expenseapi.exceptions.ResourceNotFoundException;
import com.klymenko.expenseapi.mappers.CategoryMapper;
import com.klymenko.expenseapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());

        return list.stream().map(categoryMapper::mapToCategoryDTO).toList();
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Boolean existsByNameAndUserId = categoryRepository.existsByNameAndUserId(
                        categoryDTO.getName(),
                        userService.getLoggedInUser().getId()
                );

        if (existsByNameAndUserId) {
            throw new ItemAlreadyExistsException(
                    "Category is already present for the name " + categoryDTO.getName()
            );
        }

        CategoryEntity newCategory = categoryMapper.mapToCategoryEntity(categoryDTO);
        newCategory.setCategoryId(UUID.randomUUID().toString());
        newCategory.setUser(userService.getLoggedInUser());
        newCategory = categoryRepository.save(newCategory);
        return categoryMapper.mapToCategoryDTO(newCategory);
    }

    @Override
    public void deleteCategory(String categoryId) {
        CategoryEntity category = categoryRepository
                .findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id %s is not found".formatted(categoryId))
                );
        categoryRepository.delete(category);
    }

}
