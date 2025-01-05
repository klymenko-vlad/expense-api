package com.klymenko.expenseapi.controller;

import com.klymenko.expenseapi.entity.Expense;
import com.klymenko.expenseapi.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController()
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public Page<Expense> getAllExpenses(Pageable page) {
        return expenseService.getAllExpenses(page);
    }

    @GetMapping("/{id}")
    public Expense getExpenseById(@PathVariable("id") Long id) {
        return expenseService.getExpenseById(id);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteExpenseById(@PathVariable("id") Long id) {
        expenseService.deleteExpenseById(id);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Expense saveExpenseDetails(@Valid @RequestBody Expense expense) {
        return expenseService.saveExpenseDetails(expense);
    }

    @PutMapping("/{id}")
    public Expense updateExpenseDetails(@RequestBody Expense expense, @PathVariable("id") Long id) {
        return expenseService.updateExpenseDetails(id, expense);
    }

    @GetMapping("/category")
    public List<Expense> getExpensesByCategory(@RequestParam String category, Pageable page) {
        return expenseService.readByCategory(category, page);
    }

    @GetMapping("/name")
    public List<Expense> getExpenseByName(@RequestParam String keyword, Pageable page) {
        return expenseService.readByName(keyword, page);
    }

    @GetMapping("/date")
    public List<Expense> getAllExpensesByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateBefore,
            Pageable page
    ) {
        return expenseService.readByDate(dateAfter, dateBefore, page);
    }
}
