package com.klymenko.expenseapi.controller;

import com.klymenko.expenseapi.dto.CategoryDTO;
import com.klymenko.expenseapi.dto.ExpenseDTO;
import com.klymenko.expenseapi.io.CategoryResponse;
import com.klymenko.expenseapi.io.ExpenseRequest;
import com.klymenko.expenseapi.io.ExpenseResponse;
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

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<ExpenseResponse> getAllExpenses(Pageable page) {
        List<ExpenseDTO> listOfExpenses = expenseService.getAllExpenses(page);
        return listOfExpenses.stream().map(this::mapToResponse).toList();
    }

    @GetMapping("/{expenseId}")
    public ExpenseResponse getExpenseById(@PathVariable("expenseId") String expenseId) {
        ExpenseDTO expenseDTO = expenseService.getExpenseById(expenseId);
        return mapToResponse(expenseDTO);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{expenseId}")
    public void deleteExpenseById(@PathVariable("expenseId") String expenseId) {
        expenseService.deleteExpenseById(expenseId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public ExpenseResponse saveExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest) {
        ExpenseDTO expenseDTO = mapToDTO(expenseRequest);
        expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
        return mapToResponse(expenseDTO);
    }

    private ExpenseResponse mapToResponse(ExpenseDTO expenseDTO) {
        return ExpenseResponse.builder()
                .name(expenseDTO.getName())
                .description(expenseDTO.getDescription())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .category(mapToCategoryResponse(expenseDTO.getCategoryDTO()))
                .expenseId(expenseDTO.getExpenseId())
                .createdAt(expenseDTO.getCreatedAt())
                .updatedAt(expenseDTO.getUpdatedAt())
                .build();

    }

    private CategoryResponse mapToCategoryResponse(CategoryDTO categoryDTO) {
        return CategoryResponse.builder()
                .categoryId(categoryDTO.getCategoryId())
                .name(categoryDTO.getName())
                .build();
    }

    private ExpenseDTO mapToDTO(@Valid ExpenseRequest expenseRequest) {
        return ExpenseDTO.builder()
                .name(expenseRequest.getName())
                .description(expenseRequest.getDescription())
                .amount(expenseRequest.getAmount())
                .date(expenseRequest.getDate())
                .categoryId(expenseRequest.getCategoryId())
                .build();
    }

    @PutMapping("/{expenseId}")
    public ExpenseResponse updateExpenseDetails(@RequestBody ExpenseRequest expenseRequest, @PathVariable("expenseId") String expenseId) {

        ExpenseDTO updatedExpense = expenseService.updateExpenseDetails(expenseId, mapToDTO(expenseRequest));
        return mapToResponse(updatedExpense);
    }

    @GetMapping("/category")
    public List<ExpenseResponse> getExpensesByCategory(@RequestParam String category, Pageable page) {
        List<ExpenseDTO> expenseDTOList = expenseService.readByCategory(category, page);
        return expenseDTOList.stream().map(this::mapToResponse).toList();
    }

    @GetMapping("/name")
    public List<ExpenseResponse> getExpenseByName(@RequestParam String keyword, Pageable page) {
        List<ExpenseDTO> expensesList = expenseService.readByName(keyword, page);
        return expensesList.stream().map(this::mapToResponse).toList();
    }

    //TODO: REWORK DATA ACCEPTING(IT CAN'T ACCEPT SOME TYPE OF STRING DATE)
    @GetMapping("/date")
    public List<ExpenseResponse> getAllExpensesByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateBefore,
            Pageable page
    ) {
        List<ExpenseDTO> expenseList = expenseService.readByDate(dateAfter, dateBefore, page);
        return expenseList.stream().map(this::mapToResponse).toList();
    }
}
