package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.dto.ExpenseDTO;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;


public interface ExpenseService {

    List<ExpenseDTO> getAllExpenses(Pageable page);

    ExpenseDTO getExpenseById(String expenseId);

    void deleteExpenseById(String expenseId);

    ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO);

    ExpenseDTO updateExpenseDetails(String expenseId, ExpenseDTO expenseDTO);

    List<ExpenseDTO> readByCategory(String category, Pageable page);

    List<ExpenseDTO> readByName(String keyword, Pageable page);

    List<ExpenseDTO> readByDate(Date dateAfter, Date dateBefore, Pageable pageable);

}
