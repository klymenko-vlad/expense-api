package com.klymenko.expenseapi.mappers;

import com.klymenko.expenseapi.dto.CategoryDTO;
import com.klymenko.expenseapi.entity.CategoryEntity;
import com.klymenko.expenseapi.io.CategoryRequest;
import com.klymenko.expenseapi.io.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryEntity mapToCategoryEntity(CategoryDTO categoryDTO);

    CategoryDTO mapToCategoryDTO(CategoryEntity category);

    @Mapping(target = "categoryIcon", source = "icon")
    CategoryDTO mapToCategoryDTO(CategoryRequest categoryRequest);

    CategoryResponse mapToCategoryResponse(CategoryDTO categoryDTO);

}
