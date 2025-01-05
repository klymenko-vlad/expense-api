package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.entity.Expense;
import com.klymenko.expenseapi.exceptions.ResourceNotFoundException;
import com.klymenko.expenseapi.repository.ExpenseRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepo;

    public ExpenseServiceImpl(ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    @Override
    public Page<Expense> getAllExpenses(Pageable page) {
        return expenseRepo.findAll(page);
    }

    @Override
    public Expense getExpenseById(Long id) {
        Optional<Expense> expense = expenseRepo.findById(id);

        if (expense.isPresent()) {
            return expense.get();
        }

        throw new ResourceNotFoundException("Expense with id %s is not found".formatted(id));
    }

    @Override
    public void deleteExpenseById(Long id) {
        Expense expense = getExpenseById(id);
        expenseRepo.delete(expense);
    }

    @Override
    public Expense saveExpenseDetails(Expense expense) {
        return expenseRepo.save(expense);
    }

    @Override
    public Expense updateExpenseDetails(Long id, Expense expense) {
        Expense existingExpense = getExpenseById(id);

        existingExpense.setName(expense.getName() != null ? expense.getName() : existingExpense.getName());
        existingExpense.setCategory(expense.getCategory() != null ? expense.getCategory() : existingExpense.getCategory());
        existingExpense.setDescription(expense.getDescription() != null ? expense.getDescription() : existingExpense.getDescription());
        existingExpense.setAmount(expense.getAmount() != null ? expense.getAmount() : existingExpense.getAmount());
        existingExpense.setDate(expense.getDate() != null ? expense.getDate() : existingExpense.getDate());

        return expenseRepo.save(existingExpense);
    }

    @Override
    public List<Expense> readByCategory(String category, Pageable page) {
        return expenseRepo.findByCategory(category, page).stream().toList();
    }

    @Override
    public List<Expense> readByName(String keyword, Pageable page) {
        return expenseRepo.findByNameContaining(keyword, page).stream().toList();
    }

    @Override
    public List<Expense> readByDate(Date dateAfter, Date dateBefore, Pageable page) {
        if (dateAfter == null) {
            dateAfter = new Date(0);
        }
        if (dateBefore == null) {
            dateBefore = new Date(System.currentTimeMillis());
        }

        Page<Expense> pages = expenseRepo.findByDateBetween(dateAfter, dateBefore, page) ;

        return pages.stream().toList();
    }


}
