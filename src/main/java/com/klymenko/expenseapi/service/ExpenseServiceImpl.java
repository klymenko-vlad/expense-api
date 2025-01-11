package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.dto.CategoryDTO;
import com.klymenko.expenseapi.dto.ExpenseDTO;
import com.klymenko.expenseapi.entity.CategoryEntity;
import com.klymenko.expenseapi.entity.Expense;
import com.klymenko.expenseapi.exceptions.ResourceNotFoundException;
import com.klymenko.expenseapi.repository.CategoryRepository;
import com.klymenko.expenseapi.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepo;
    private final UserService userService;

    @Override
    public List<ExpenseDTO> getAllExpenses(Pageable page) {
        List<Expense> listOfExpenses = expenseRepo.findByUserId(userService.getLoggedInUser().getId(), page).stream().toList();
        return listOfExpenses.stream().map(this::mapToDTO).toList();
    }

    @Override
    public ExpenseDTO getExpenseById(String expenseId) {
        Expense expense = getExpenseEntity(expenseId);

        return mapToDTO(expense);

    }

    private Expense getExpenseEntity(String expenseId) {
        return expenseRepo.findByUserIdAndExpenseId(
                userService.getLoggedInUser().getId(),
                expenseId
        ).orElseThrow(() ->
                new ResourceNotFoundException("Expense with id %s is not found".formatted(expenseId))
        );
    }

    @Override
    public void deleteExpenseById(String expenseId) {
        Expense expense = getExpenseEntity(expenseId);
        expenseRepo.delete(expense);
    }

    @Override
    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {
        CategoryEntity category = categoryRepository.findByUserIdAndCategoryId(
                userService.getLoggedInUser().getId(),
                expenseDTO.getCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Category with %s id is not found"
                        .formatted(expenseDTO.getCategoryId()))
        );

        expenseDTO.setExpenseId(UUID.randomUUID().toString());
        Expense newExpense = mapToEntity(expenseDTO);
        newExpense.setCategory(category);
        newExpense.setUser(userService.getLoggedInUser());
        newExpense = expenseRepo.save(newExpense);
        return mapToDTO(newExpense);
    }

    private ExpenseDTO mapToDTO(Expense newExpense) {
        return ExpenseDTO.builder()
                .expenseId(newExpense.getExpenseId())
                .name(newExpense.getName())
                .description(newExpense.getDescription())
                .amount(newExpense.getAmount())
                .date(newExpense.getDate())
                .createdAt(newExpense.getCreatedAt())
                .updatedAt(newExpense.getUpdatedAt())
                .categoryDTO(mapToCategoryDTO(newExpense.getCategory()))
                .build();
    }

    private CategoryDTO mapToCategoryDTO(CategoryEntity category) {
        return CategoryDTO.builder()
                .name(category.getName())
                .categoryId(category.getCategoryId())
                .build();
    }

    private Expense mapToEntity(ExpenseDTO expenseDTO) {
        return Expense.builder()
                .expenseId(expenseDTO.getExpenseId())
                .name(expenseDTO.getName())
                .description(expenseDTO.getDescription())
                .date(expenseDTO.getDate())
                .amount(expenseDTO.getAmount())
                .build();
    }

    @Override
    public ExpenseDTO updateExpenseDetails(String expenseId, ExpenseDTO expenseDTO) {
        Expense existingExpense = getExpenseEntity(expenseId);

        if (expenseDTO.getCategoryId() != null) {
            CategoryEntity category = categoryRepository
                    .findByUserIdAndCategoryId(
                            userService.getLoggedInUser().getId(),
                            expenseDTO.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category with id %s is not found"
                                    .formatted(expenseDTO.getCategoryId())));
            existingExpense.setCategory(category);
        }

        existingExpense.setName(expenseDTO.getName() != null ? expenseDTO.getName() : existingExpense.getName());
        existingExpense.setDescription(expenseDTO.getDescription() != null ? expenseDTO.getDescription() : existingExpense.getDescription());
        existingExpense.setAmount(expenseDTO.getAmount() != null ? expenseDTO.getAmount() : existingExpense.getAmount());
        existingExpense.setDate(expenseDTO.getDate() != null ? expenseDTO.getDate() : existingExpense.getDate());

        existingExpense = expenseRepo.save(existingExpense);
        return mapToDTO(existingExpense);
    }

    @Override
    public List<ExpenseDTO> readByCategory(String category, Pageable page) {

        CategoryEntity categoryEntity = categoryRepository.findByNameAndUserId(category, userService.getLoggedInUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with name %s is not found".formatted(category)));

        List<Expense> list = expenseRepo.findByUserIdAndCategoryId(
                userService.getLoggedInUser().getId(),
                categoryEntity.getId(),
                page
        ).stream().toList();
        return list.stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<ExpenseDTO> readByName(String keyword, Pageable page) {
        List<Expense> list = expenseRepo.findByUserIdAndNameContaining(
                userService.getLoggedInUser().getId(),
                keyword,
                page
        ).stream().toList();

        return list.stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<ExpenseDTO> readByDate(Date dateAfter, Date dateBefore, Pageable page) {
        if (dateAfter == null) {
            dateAfter = new Date(0);
        }
        if (dateBefore == null) {
            dateBefore = new Date(System.currentTimeMillis());
        }

        Page<Expense> expenseList = expenseRepo.findByUserIdAndDateBetween(
                userService.getLoggedInUser().getId(),
                dateAfter,
                dateBefore,
                page
        );

        return expenseList.stream().map(this::mapToDTO).toList();
    }


}
