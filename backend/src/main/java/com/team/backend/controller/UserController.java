package com.team.backend.controller;

import com.team.backend.config.JwtProvider;
import com.team.backend.config.JwtResponse;
import com.team.backend.model.User;
import com.team.backend.model.helpers.LoginForm;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;
    private JwtProvider jwtProvider;
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@Valid @RequestBody User user) {
        if (userService.existsByLogin(user.getLogin()) || userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Given user has an account!", HttpStatus.BAD_REQUEST);
        }

        userService.save(user);

        return ResponseEntity.ok("User has been created");
    }
}
