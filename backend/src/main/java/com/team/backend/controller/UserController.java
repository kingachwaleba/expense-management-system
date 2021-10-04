package com.team.backend.controller;

import com.team.backend.config.JwtProvider;
import com.team.backend.config.JwtResponse;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.helpers.UpdatePasswordHolder;
import com.team.backend.model.*;
import com.team.backend.helpers.LoginForm;
import com.team.backend.service.ImageStorageService;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final ImageStorageService imageStorageService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, JwtProvider jwtProvider,
                          AuthenticationManager authenticationManager, ImageStorageService imageStorageService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.imageStorageService = imageStorageService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        String image = userService.findByLogin(userDetails.getUsername()).orElseThrow(UserNotFoundException::new)
                .getImage();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), image, userDetails.getAuthorities()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@Valid @RequestBody User user) {
        if (userService.existsByEmailAndDeleted(user.getEmail(), String.valueOf(User.AccountType.valueOf("N")))
                || userService.existsByLogin(user.getLogin())) {
            return new ResponseEntity<>("Given user has an account!", HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmailAndDeleted(user.getEmail(), String.valueOf(User.AccountType.valueOf("Y"))))
            userService.saveAgain(
                    userService.findByEmail(user.getEmail()).orElseThrow(UserNotFoundException::new),
                    user
            );
        else
            userService.save(user);

        return ResponseEntity.ok("User has been created");
    }

    @PutMapping("/account/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UpdatePasswordHolder updatePasswordHolder) {
        String password = updatePasswordHolder.getPassword();
        String oldPassword = updatePasswordHolder.getOldPassword();

        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        if (!userService.checkIfValidOldPassword(user, oldPassword))
            return new ResponseEntity<>("Wrong password has been given!", HttpStatus.CONFLICT);

        userService.changeUserPassword(user, password);

        return new ResponseEntity<>("User password has been changed!", HttpStatus.OK);
    }

    @PutMapping("/account/change-profile-picture")
    public ResponseEntity<?> changeImage(@RequestParam("image") MultipartFile multipartFile) {
        if (!imageStorageService.ifProperType(multipartFile))
            return new ResponseEntity<>("It is not a proper type!", HttpStatus.EXPECTATION_FAILED);

        try {
            String newImageName = imageStorageService.save(multipartFile, "users");
            User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

            userService.changeUserImage(user, newImageName);

            return new ResponseEntity<>("User image has been changed!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody String password) {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        if (!userService.checkIfValidOldPassword(user, password))
            return new ResponseEntity<>("Wrong password has been given!", HttpStatus.CONFLICT);

        if (!userService.ifAccountDeleted(user))
            return new ResponseEntity<>("Cannot delete account!", HttpStatus.CONFLICT);
        else
            return new ResponseEntity<>("Account has been deleted!", HttpStatus.OK);
    }
}
