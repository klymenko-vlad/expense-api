package com.klymenko.expenseapi.repository;

import com.klymenko.expenseapi.entity.Expense;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserIdAndCategoryId(
            Long userId, Long categoryId, Pageable pageable
    );


    Page<Expense> findByUserIdAndNameContaining(
            Long userId,
            @NotBlank(message = "Expense name can't be null")
            @Size(min = 2, message = "Expense name needs to be at least 2 characters")
            String keyword,
            Pageable pageable
    );

    Page<Expense> findByUserIdAndDateBetween(
            Long userId,
            @NotNull(message = "Date can't be null") Date dateAfter,
            @NotNull(message = "Date can't be null") Date dateBefore,
            Pageable pageable
    );

    Page<Expense> findByUserId(Long userId, Pageable page);

    Optional<Expense> findByUserIdAndExpenseId(Long userId, String expenseId);
}
