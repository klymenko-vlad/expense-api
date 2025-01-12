package com.klymenko.expenseapi.controller;

import com.klymenko.expenseapi.dto.ExpenseDTO;
import com.klymenko.expenseapi.io.ExpenseRequest;
import com.klymenko.expenseapi.io.ExpenseResponse;
import com.klymenko.expenseapi.mappers.ExpenseMapper;
import com.klymenko.expenseapi.service.ExpenseService;
import jakarta.validation.Valid;
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

    private final ExpenseMapper expenseMapper;

    public ExpenseController(ExpenseService expenseService, ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
    }

    @GetMapping
    public List<ExpenseResponse> getAllExpenses(Pageable page) {
        List<ExpenseDTO> listOfExpenses = expenseService.getAllExpenses(page);
        return listOfExpenses.stream().map(expenseMapper::mapToExpenseResponse).toList();
    }

    @GetMapping("/{expenseId}")
    public ExpenseResponse getExpenseById(@PathVariable("expenseId") String expenseId) {
        ExpenseDTO expenseDTO = expenseService.getExpenseById(expenseId);
        return expenseMapper.mapToExpenseResponse(expenseDTO);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{expenseId}")
    public void deleteExpenseById(@PathVariable("expenseId") String expenseId) {
        expenseService.deleteExpenseById(expenseId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public ExpenseResponse saveExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest) {
        ExpenseDTO expenseDTO = expenseMapper.mapToExpenseDTO(expenseRequest);
        expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
        return expenseMapper.mapToExpenseResponse(expenseDTO);
    }

    @PutMapping("/{expenseId}")
    public ExpenseResponse updateExpenseDetails(@RequestBody ExpenseRequest expenseRequest, @PathVariable("expenseId") String expenseId) {

        ExpenseDTO updatedExpense = expenseService.updateExpenseDetails(expenseId, expenseMapper.mapToExpenseDTO(expenseRequest));
        return expenseMapper.mapToExpenseResponse(updatedExpense);
    }

    @GetMapping("/category")
    public List<ExpenseResponse> getExpensesByCategory(@RequestParam String category, Pageable page) {
        List<ExpenseDTO> expenseDTOList = expenseService.readByCategory(category, page);
        return expenseDTOList.stream().map(expenseMapper::mapToExpenseResponse).toList();
    }

    @GetMapping("/name")
    public List<ExpenseResponse> getExpenseByName(@RequestParam String keyword, Pageable page) {
        List<ExpenseDTO> expensesList = expenseService.readByName(keyword, page);
        return expensesList.stream().map(expenseMapper::mapToExpenseResponse).toList();
    }

    //TODO: REWORK DATA ACCEPTING(IT CAN'T ACCEPT SOME TYPE OF STRING DATE)
    @GetMapping("/date")
    public List<ExpenseResponse> getAllExpensesByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateBefore,
            Pageable page
    ) {
        List<ExpenseDTO> expenseList = expenseService.readByDate(dateAfter, dateBefore, page);
        return expenseList.stream().map(expenseMapper::mapToExpenseResponse).toList();
    }
}
