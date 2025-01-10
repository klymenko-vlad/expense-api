package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.dto.CategoryDTO;
import com.klymenko.expenseapi.dto.UserDTO;
import com.klymenko.expenseapi.entity.CategoryEntity;
import com.klymenko.expenseapi.entity.User;
import com.klymenko.expenseapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.expenseapi.exceptions.ResourceNotFoundException;
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

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());

        return list.stream().map(this::mapToDTO).toList();
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

        CategoryEntity newCategory = mapToEntity(categoryDTO);
        newCategory = categoryRepository.save(newCategory);
        return mapToDTO(newCategory);
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

    private CategoryEntity mapToEntity(CategoryDTO categoryDTO) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .categoryIcon(categoryDTO.getCategoryIcon())
                .categoryId(String.valueOf(UUID.randomUUID()))
                .user(userService.getLoggedInUser())
                .build();
    }

    private CategoryDTO mapToDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .categoryId(categoryEntity.getCategoryId())
                .name(categoryEntity.getName())
                .description(categoryEntity.getDescription())
                .categoryIcon(categoryEntity.getCategoryIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .user(mapToUserDto(categoryEntity.getUser()))
                .build();
    }

    private UserDTO mapToUserDto(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }


}
