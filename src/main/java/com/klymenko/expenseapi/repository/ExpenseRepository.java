package com.klymenko.expenseapi.repository;

import com.klymenko.expenseapi.entity.Expense;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByCategory(
            @NotBlank(message = "Category can't be null") String category,
            Pageable page
    );

    Page<Expense> findByNameContaining(
            @NotBlank(message = "Expense name can't be null")
            @Size(min = 2, message = "Expense name needs to be at least 2 characters")
            String keyword,
            Pageable pageable
    );

    Page<Expense> findByDateBetween(
            @NotNull(message = "Date can't be null") Date dateAfter,
            @NotNull(message = "Date can't be null") Date dateBefore,
            Pageable pageable
    );
}
