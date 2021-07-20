package com.team.backend.controller;

import com.team.backend.model.User;
import com.team.backend.service.SecurityService;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@Valid @RequestBody User user) {
        if (userService.existsByLogin(user.getLogin()) || userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Given user has an account!", HttpStatus.BAD_REQUEST);
        }

        userService.save(user);
//        securityService.autoLogin(user.getLogin(), user.getPassword());

        return ResponseEntity.ok("User has been created");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
//        if (securityService.isAuthenticated()) {
//            return new ResponseEntity<>(HttpStatus.OK);
//        }

        Principal principal = request.getUserPrincipal();
        if (principal == null || principal.getName() == null)
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        User user = userService.findByLogin(principal.getName()).orElseThrow(RuntimeException::new);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
