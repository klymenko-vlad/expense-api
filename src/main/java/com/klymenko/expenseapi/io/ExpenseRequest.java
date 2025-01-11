package com.klymenko.expenseapi.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseRequest {

    @NotBlank(message = "Expense name can't be null")
    @Size(min = 3, message = "Expense name needs to be at least 3 characters")
    private String name;

    private String description;

    @NotNull(message = "Expense amount can't be null")
    private BigDecimal amount;

    @NotBlank(message = "Category can't be null")
    private String categoryId;

    @NotNull(message = "Date can't be null")
    private Date date;
}
