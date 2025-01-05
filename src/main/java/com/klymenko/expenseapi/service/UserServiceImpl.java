package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.entity.User;
import com.klymenko.expenseapi.entity.UserModel;
import com.klymenko.expenseapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.expenseapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserModel user) {
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ItemAlreadyExistsException("User is already exist with email %s".formatted(user.getEmail()));
        }

        return userRepository.save(newUser);
    }
}
