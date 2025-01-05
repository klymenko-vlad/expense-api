package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.entity.User;
import com.klymenko.expenseapi.entity.UserModel;

public interface UserService {
    User createUser(UserModel user);
}
