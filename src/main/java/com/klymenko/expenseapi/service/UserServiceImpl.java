package com.klymenko.expenseapi.service;

import com.klymenko.expenseapi.entity.User;
import com.klymenko.expenseapi.entity.UserModel;
import com.klymenko.expenseapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.expenseapi.exceptions.ResourceNotFoundException;
import com.klymenko.expenseapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserModel user) {
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ItemAlreadyExistsException("User is already exist with email %s".formatted(user.getEmail()));
        }

        return userRepository.save(newUser);
    }

    @Override
    public User readUser() {
        return userRepository
                .findById(getLoggedInUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id %s is not found".formatted(getLoggedInUser().getId())));
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User is not found for the email " + email)
                );
    }

    @Override
    public User updateUser(UserModel user) {
        User existingUser = readUser();

        existingUser.setName(user.getName() != null ? user.getName() : existingUser.getName());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
        existingUser.setAge(user.getAge() != null ? user.getAge() : existingUser.getAge());
        existingUser.setPassword(user.getPassword() != null
                ? bCryptPasswordEncoder.encode(user.getPassword())
                : existingUser.getPassword()
        );

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser() {
        User user = readUser();

        userRepository.delete(user);
    }



}
