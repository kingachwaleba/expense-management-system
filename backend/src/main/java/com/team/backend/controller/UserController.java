package com.team.backend.controller;

import com.fasterxml.jackson.databind.node.TextNode;
import com.team.backend.config.ErrorMessage;
import com.team.backend.config.JwtProvider;
import com.team.backend.config.JwtResponse;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.helpers.RegistrationForm;
import com.team.backend.helpers.UpdatePasswordHolder;
import com.team.backend.model.*;
import com.team.backend.helpers.LoginForm;
import com.team.backend.service.ImageStorageService;
import com.team.backend.service.JavaMailService;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final ImageStorageService imageStorageService;
    private final JavaMailService javaMailService;
    private final ErrorMessage errorMessage;

    public UserController(UserService userService, JwtProvider jwtProvider,
                          AuthenticationManager authenticationManager, ImageStorageService imageStorageService,
                          JavaMailService javaMailService, ErrorMessage errorMessage) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.imageStorageService = imageStorageService;
        this.javaMailService = javaMailService;
        this.errorMessage = errorMessage;
    }

    @GetMapping("/find-users/{infix}")
    public ResponseEntity<?> findUser(@PathVariable String infix) {

        return new ResponseEntity<>(userService.findUser(infix), HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/{infix}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> findUserForWallet(@PathVariable int id, @PathVariable String infix) {

        return new ResponseEntity<>(userService.findUserForWallet(id, infix), HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<?> one() {

        return new ResponseEntity<>(userService.findUserDetails(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest, BindingResult bindingResult) {
        if (userService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty() || (optionalUser.get().getDeleted().equals(String.valueOf(User.AccountType.Y))
                || !userService.checkIfValidOldPassword(optionalUser.get(), loginRequest.getPassword())))
            return new ResponseEntity<>(errorMessage.get("login.error"), HttpStatus.UNAUTHORIZED);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtProvider.generateJwtToken(authentication);
        Date expiryDate = jwtProvider.getExpiryDateFromJwtToken(jwtToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String image = userService.findByLogin(userDetails.getUsername()).orElseThrow(UserNotFoundException::new)
                .getImage();

        return ResponseEntity.ok(new JwtResponse(jwtToken, expiryDate, userDetails.getUsername(), image));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody RegistrationForm registrationForm,
                                           BindingResult bindingResult) {
        User user = registrationForm.getUser();
        String confirmPassword = registrationForm.getConfirmPassword();

        if (userService.validation(bindingResult, user.getPassword()).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        if (!userService.checkIfValidConfirmPassword(user.getPassword(), confirmPassword))
            return new ResponseEntity<>(errorMessage.get("register.confirmPassword"), HttpStatus.BAD_REQUEST);

        if (userService.existsByEmailAndDeleted(user.getEmail(), String.valueOf(User.AccountType.valueOf("N")))
                || userService.existsByLogin(user.getLogin()))
            return new ResponseEntity<>(errorMessage.get("register.takenCredentials"), HttpStatus.CONFLICT);

        userService.saveAccount(user);

        return ResponseEntity.ok("User has been created");
    }

    @PostMapping("/account/forgot-password")
    public ResponseEntity<?> forgotPassword(HttpServletRequest request, @RequestParam("email") String email) {
        if (!userService.checkIfValidEmail(email))
            return new ResponseEntity<>(errorMessage.get("forgotPassword.email.error"), HttpStatus.BAD_REQUEST);

        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty() || optionalUser.get().getDeleted().equals(String.valueOf(User.AccountType.Y)))
            return new ResponseEntity<>(errorMessage.get("account.error"),
                    HttpStatus.BAD_REQUEST);

        User user = optionalUser.get();
        String appUrl = request.getScheme() + "://" + request.getServerName();

        userService.setTokenAndExpiryDate(user);
        javaMailService.sendMessage(user, appUrl);

        return new ResponseEntity<>("The forgot password token was created and email was sent!", HttpStatus.OK);
    }

    @PostMapping("/account/reset-password")
    public ResponseEntity<?> setNewPassword(@RequestParam("token") String token,
                                            @RequestParam("password") String password,
                                            @RequestParam("confirmPassword") String confirmPassword) {
        if (!userService.checkIfValidExpiryDate(token))
            return new ResponseEntity<>(errorMessage.get("resetPassword.expiredToken"), HttpStatus.BAD_REQUEST);

        if (userService.passwordValidation(password).size() != 0)
            return new ResponseEntity<>(errorMessage.get("user.password.error"), HttpStatus.BAD_REQUEST);

        if (!userService.checkIfValidConfirmPassword(password, confirmPassword))
            return new ResponseEntity<>(errorMessage.get("resetPassword.wrongPasswords"), HttpStatus.BAD_REQUEST);

        userService.resetPassword(token, password);

        return new ResponseEntity<>("Password was changed!", HttpStatus.OK);
    }

    @PutMapping("/account/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UpdatePasswordHolder updatePasswordHolder,
                                            BindingResult bindingResult) {
        String password = updatePasswordHolder.getPassword();
        String confirmPassword = updatePasswordHolder.getConfirmPassword();
        String oldPassword = updatePasswordHolder.getOldPassword();

        if (userService.validation(bindingResult, password).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        if (!userService.checkIfValidConfirmPassword(password, confirmPassword))
            return new ResponseEntity<>(errorMessage.get("resetPassword.wrongPasswords"), HttpStatus.BAD_REQUEST);

        if (!userService.checkIfValidOldPassword(user, oldPassword))
            return new ResponseEntity<>(errorMessage.get("changePassword.oldPassword"), HttpStatus.BAD_REQUEST);

        userService.changeUserPassword(user, password);

        return new ResponseEntity<>("User password has been changed!", HttpStatus.OK);
    }

    @PutMapping("/account/change-profile-picture")
    public ResponseEntity<?> changeImage(@RequestParam("image") MultipartFile multipartFile) {
        if (!imageStorageService.ifProperType(multipartFile))
            return new ResponseEntity<>(errorMessage.get("image.type"), HttpStatus.EXPECTATION_FAILED);

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

    @PutMapping("/account/delete-profile-picture")
    public ResponseEntity<?> deleteImage() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        userService.changeUserImage(user, null);

        return new ResponseEntity<>("User image has been deleted!", HttpStatus.OK);
    }

    @PutMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody TextNode password) {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        if (!userService.checkIfValidOldPassword(user, password.asText()))
            return new ResponseEntity<>(errorMessage.get("deleteAccount.password"), HttpStatus.BAD_REQUEST);

        if (!userService.ifAccountDeleted(user))
            return new ResponseEntity<>(errorMessage.get("deleteAccount.error"), HttpStatus.CONFLICT);
        else
            return new ResponseEntity<>("Account has been deleted!", HttpStatus.OK);
    }
}
