package com.klymenko.expenseapi.controller;

import com.klymenko.expenseapi.entity.LoginModel;
import com.klymenko.expenseapi.entity.User;
import com.klymenko.expenseapi.entity.UserModel;
import com.klymenko.expenseapi.security.JwtUtil;
import com.klymenko.expenseapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginModel login) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        return new ResponseEntity<>(jwtUtils.generateToken(userDetails.getUsername()), HttpStatus.OK);
    }

    @PostMapping("/register")
    private ResponseEntity<User> save(@Valid @RequestBody UserModel user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

}
