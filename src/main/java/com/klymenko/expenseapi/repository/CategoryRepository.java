package com.klymenko.expenseapi.repository;

import com.klymenko.expenseapi.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUserId(Long userId);

    Optional<CategoryEntity> findByUserIdAndCategoryId(Long userId, String categoryId);

    Optional<CategoryEntity> findByNameAndUserId(String name, Long userId);
    
    Boolean existsByNameAndUserId(String name, Long userId);
}
