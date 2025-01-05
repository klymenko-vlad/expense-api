package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.entity.User;
import com.klymenko.expenseapi.entity.UserModel;
import com.klymenko.expenseapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.expenseapi.exceptions.ResourceNotFoundException;
import com.klymenko.expenseapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;

@Service
public class UserServiceImpl implements UserService {

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

    @Override
    public User readUser(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id %s is not found".formatted(id)));
    }

    @Override
    public User updateUser(UserModel user, Long id) {
        User existingUser = readUser(id);

        existingUser.setName(user.getName() != null ? user.getName() : existingUser.getName());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
        existingUser.setAge(user.getAge() != null ? user.getAge() : existingUser.getAge());
        existingUser.setPassword(user.getPassword() != null ? user.getPassword() : existingUser.getPassword());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = readUser(id);

        userRepository.delete(user);
    }


}
