package com.team.backend.controller;

import com.team.backend.config.JwtProvider;
import com.team.backend.config.JwtResponse;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.helpers.UpdatePasswordHolder;
import com.team.backend.model.*;
import com.team.backend.helpers.LoginForm;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtProvider jwtProvider,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/find-users/{infix}")
    public ResponseEntity<?> findUser(@PathVariable String infix) {

        return new ResponseEntity<>(userService.findUser(infix), HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/{infix}")
    @PreAuthorize("@authenticationService.isWalletOwner(#id)")
    public ResponseEntity<?> findUserForWallet(@PathVariable int id, @PathVariable String infix) {

        return new ResponseEntity<>(userService.findUserForWallet(id, infix), HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<?> one() {

        return new ResponseEntity<>(userService.findUserDetails(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        if (userService.findByEmail(loginRequest.getEmail()).isPresent()) {
            User user = userService.findByEmail(loginRequest.getEmail()).get();

            if (user.getDeleted().equals(String.valueOf(User.AccountType.Y)))
                return new ResponseEntity<>("This account has been deleted!", HttpStatus.CONFLICT);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

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

    @PutMapping("/account/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UpdatePasswordHolder updatePasswordHolder) {
        String password = updatePasswordHolder.getPassword();
        String oldPassword = updatePasswordHolder.getOldPassword();

        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        if (!userService.checkIfValidOldPassword(user, oldPassword))
            throw new RuntimeException();

        userService.changeUserPassword(user, password);

        return new ResponseEntity<>("User password has been changed!", HttpStatus.OK);
    }

    @PutMapping("/account/change-profile-picture")
    public ResponseEntity<?> changeImage(@RequestBody String imageUrl) {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        userService.changeUserImage(user, imageUrl);

        return new ResponseEntity<>("User image has been changed!", HttpStatus.OK);
    }

    @PutMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody String password) {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        if (!userService.checkIfValidOldPassword(user, password))
            throw new RuntimeException();

        if (!userService.ifAccountDeleted(user))
            return new ResponseEntity<>("Cannot delete account!", HttpStatus.CONFLICT);
        else
            return new ResponseEntity<>("Account has been deleted!", HttpStatus.OK);
    }
}
