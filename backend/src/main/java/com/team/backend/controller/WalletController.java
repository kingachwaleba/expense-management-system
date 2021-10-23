package com.team.backend.controller;

import com.team.backend.config.ErrorMessage;
import com.team.backend.exception.*;
import com.team.backend.helpers.DebtsHolder;
import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.MessageService;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;
    private final UserStatusRepository userStatusRepository;
    private final WalletUserRepository walletUserRepository;
    private final MessageService messageService;
    private final ErrorMessage errorMessage;

    public WalletController(WalletService walletService, UserService userService,
                            UserStatusRepository userStatusRepository, WalletUserRepository walletUserRepository,
                            MessageService messageService, ErrorMessage errorMessage) {
        this.walletService = walletService;
        this.userService = userService;
        this.userStatusRepository = userStatusRepository;
        this.walletUserRepository = walletUserRepository;
        this.messageService = messageService;
        this.errorMessage = errorMessage;
    }

    @GetMapping("/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> one(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        return new ResponseEntity<>(walletService.getOne(wallet), HttpStatus.OK);
    }

    @GetMapping("/wallets")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(walletService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/balance")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> getWalletBalance(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        return new ResponseEntity<>(walletService.findDebts(wallet), HttpStatus.OK);
    }

    @GetMapping("/wallet-users/{id}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> findsWalletUsers(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);
        List<Map<String, Object>> userList = walletService.findUserList(wallet);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/stats")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> getStats(@PathVariable int id, @RequestParam("dateFrom") String dateFrom,
                                      @RequestParam("dateTo") String dateTo) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        LocalDateTime from;
        LocalDateTime to;

        try {
            from = LocalDateTime.parse(dateFrom);
            to = LocalDateTime.parse(dateTo);
        } catch (DateTimeException dateTimeException) {
            return new ResponseEntity<>(dateTimeException.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (to.isBefore(from) || from.isAfter(LocalDateTime.now())
                || to.isAfter(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)))
            return new ResponseEntity<>(errorMessage.get("wallet.stats"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(walletService.returnStats(wallet, from, to), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/create-wallet")
    public ResponseEntity<?> createWallet(@Valid @RequestBody WalletHolder walletHolder, BindingResult bindingResult) {
        if (walletService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        walletService.save(walletHolder);

        return new ResponseEntity<>(walletHolder.getWallet(), HttpStatus.OK);
    }

    @PutMapping("/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletOwner(#id)")
    public ResponseEntity<?> editOne(@PathVariable int id, @Valid @RequestBody Wallet newWallet,
                                     BindingResult bindingResult) {
        if (walletService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        Wallet updatedWallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        updatedWallet.setName(newWallet.getName());
        updatedWallet.setDescription(newWallet.getDescription());
        updatedWallet.setWalletCategory(newWallet.getWalletCategory());

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @PutMapping("/wallet/{id}/users/{userLogin}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> addUsers(@PathVariable int id, @PathVariable String userLogin) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        UserStatus waitingStatus = userStatusRepository.findByName("oczekujący")
                .orElseThrow(UserStatusNotFoundException::new);

        User user = userService.findByLogin(userLogin).orElseThrow(UserNotFoundException::new);
        Optional<WalletUser> optionalWalletUser = walletUserRepository.findByWalletAndUser(updatedWallet, user);

        if (optionalWalletUser.isEmpty())
            walletService.saveUser(userLogin, updatedWallet, waitingStatus);
        else {
            WalletUser walletUser = optionalWalletUser.get();
            walletUser.setUserStatus(waitingStatus);
        }

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }
    
    @PutMapping("/pay-debt/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> payDebt(@PathVariable int id, @RequestBody DebtsHolder debtsHolder) {
        User currentUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        
        if (!currentUser.getLogin().equals(debtsHolder.getCreditor().getLogin()))
            return new ResponseEntity<>("Only creditor can tell that the debt has been paid!",
                    HttpStatus.FORBIDDEN);

        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        WalletUser debtorInfo = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getDebtor()).orElseThrow(WalletUserNotFoundException::new);
        WalletUser creditorInfo = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getCreditor()).orElseThrow(WalletUserNotFoundException::new);

        BigDecimal debt = debtsHolder.getHowMuch();
        BigDecimal newDebtorBalance = debtorInfo.getBalance().add(debt);
        BigDecimal newCreditorBalance = creditorInfo.getBalance().subtract(debt);

        if (newDebtorBalance.abs().compareTo(BigDecimal.valueOf(0.10)) < 0)
            newDebtorBalance = BigDecimal.valueOf(0.00);
        if (newCreditorBalance.abs().compareTo(BigDecimal.valueOf(0.10)) < 0)
            newCreditorBalance = BigDecimal.valueOf(0.00);

        debtorInfo.setBalance(newDebtorBalance);
        creditorInfo.setBalance(newCreditorBalance);

        walletUserRepository.save(debtorInfo);
        walletUserRepository.save(creditorInfo);
        messageService.sendNotification(wallet.getId());

        return new ResponseEntity<>("The debt has been paid!", HttpStatus.OK);
    }

    @DeleteMapping("/wallet/{id}/user/{userLogin}")
    @PreAuthorize("@authenticationService.isWalletOwner(#id)")
    public ResponseEntity<?> deleteUserFromWallet(@PathVariable int id, @PathVariable String userLogin) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);
        User user = userService.findByLogin(userLogin).orElseThrow(UserNotFoundException::new);

        if (walletService.deleteUser(wallet, user))
            return new ResponseEntity<>("User has been deleted from the wallet!", HttpStatus.OK);
        else
            return new ResponseEntity<>(errorMessage.get("wallet.deleteUser"), HttpStatus.CONFLICT);
    }

    @DeleteMapping("/wallet/{id}/current-logged-in-user")
    @PreAuthorize("@authenticationService.isWalletMember(#id) ")
    public ResponseEntity<?> deleteCurrentLoggedInUserFromWallet(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        if (walletService.deleteUser(wallet, user))
            return new ResponseEntity<>("User has been deleted from the wallet!", HttpStatus.OK);
        else
            return new ResponseEntity<>(errorMessage.get("wallet.deleteUser"), HttpStatus.CONFLICT);
    }

    @DeleteMapping("/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletOwner(#id)")
    public ResponseEntity<?> deleteOne(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        walletService.delete(wallet);

        return new ResponseEntity<>("Wallet has been deleted!", HttpStatus.OK);
    }
}
