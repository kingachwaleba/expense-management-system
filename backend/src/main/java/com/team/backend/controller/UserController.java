package com.team.backend.controller;

import com.team.backend.config.JwtProvider;
import com.team.backend.config.JwtResponse;
import com.team.backend.helpers.UpdatePasswordHolder;
import com.team.backend.model.*;
import com.team.backend.helpers.LoginForm;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
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
import java.util.*;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final WalletService walletService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, WalletService walletService, JwtProvider jwtProvider,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.walletService = walletService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/find-users/{infix}")
    public ResponseEntity<?> findUser(@PathVariable String infix) {
        User loggedInUser = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        List<User> userList = userService.findByDeletedAndLoginContaining(String.valueOf(User.AccountType.N), infix);
        List<String> userLoginList = new ArrayList<>();
        for (User user : userList) {
            if (user.getId() != loggedInUser.getId())
                userLoginList.add(user.getLogin());
        }

        return new ResponseEntity<>(userLoginList, HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/{infix}")
    @PreAuthorize("@authenticationService.isWalletOwner(#id)")
    public ResponseEntity<?> findUserForWallet(@PathVariable int id, @PathVariable String infix) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        List<Map<String, Object>> userList = walletService.findAllUsers(wallet);

        List<User> userListInfix = userService.findByDeletedAndLoginContaining(String.valueOf(User.AccountType.N),infix);
        List<String> userLoginList = new ArrayList<>();
        for (User user : userListInfix) {
            Map<String, Object> userMap = new HashMap<>();

            userMap.put("userId", user.getId());
            userMap.put("login", user.getLogin());

            if (!userList.contains(userMap))
                userLoginList.add(user.getLogin());
        }

        return new ResponseEntity<>(userLoginList, HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<?> one() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        List<Wallet> wallets = walletService.findWallets(user);

        Map<String, String> userDetailsMap = new HashMap<>();
        userDetailsMap.put("id", String.valueOf(user.getId()));
        userDetailsMap.put("login", user.getLogin());
        userDetailsMap.put("email", user.getEmail());
        userDetailsMap.put("image", user.getImage());
        userDetailsMap.put("walletsNumber", String.valueOf(wallets.size()));
        userDetailsMap.put("userBalance", String.valueOf(userService.calculateUserBalance(user)));

        return new ResponseEntity<>(userDetailsMap, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
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

        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        if (!userService.checkIfValidOldPassword(user, oldPassword))
            throw new RuntimeException();

        userService.changeUserPassword(user, password);

        return new ResponseEntity<>("User password has been changed!", HttpStatus.OK);
    }

    @PutMapping("/account/change-profile-picture")
    public ResponseEntity<?> changeImage(@RequestBody String imageUrl) {
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        userService.changeUserImage(user, imageUrl);

        return new ResponseEntity<>("User image has been changed!", HttpStatus.OK);
    }

    @PutMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody String password) {
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        if (!userService.checkIfValidOldPassword(user, password))
            throw new RuntimeException();

        if (!userService.ifAccountDeleted(user))
            return new ResponseEntity<>("Cannot delete account!", HttpStatus.CONFLICT);
        else
            return new ResponseEntity<>("Account has been deleted!", HttpStatus.OK);
    }
}
