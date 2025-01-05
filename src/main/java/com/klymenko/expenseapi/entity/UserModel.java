package com.klymenko.expenseapi.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserModel {

    @NotBlank(message = "You have to provide a valid name")
    @Size(min = 2, message = "Your name should be at least 3 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "You have to provide a valid email")
    private String email;

    @NotBlank(message = "You have to provide a valid name")
    @Size(min = 5, message = "Your password should be at least 5 characters")
    private String password;

    private Long age = 0L;

}
