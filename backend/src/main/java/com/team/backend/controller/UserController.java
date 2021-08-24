package com.team.backend.controller;

import com.team.backend.config.JwtProvider;
import com.team.backend.config.JwtResponse;
import com.team.backend.helpers.UpdatePasswordHolder;
import com.team.backend.model.User;
import com.team.backend.helpers.LoginForm;
import com.team.backend.model.Wallet;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final WalletService walletService;
    private JwtProvider jwtProvider;
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService, WalletService walletService, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.walletService = walletService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/{infix}")
    public ResponseEntity<?> findUser(@PathVariable String infix) {
        User loggedInUser = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        List<User> userList = userService.findByLoginContaining(infix);
        List<String> userLoginList = new ArrayList<>();
        for (User user : userList) {
            if (user.getId() != loggedInUser.getId())
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
}
