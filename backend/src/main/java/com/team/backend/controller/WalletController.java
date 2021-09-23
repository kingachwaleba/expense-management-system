package com.team.backend.controller;

import com.team.backend.helpers.DebtsHolder;
import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletCategoryRepository;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;
    private final UserStatusRepository userStatusRepository;
    private final WalletCategoryRepository walletCategoryRepository;

    public WalletController(WalletService walletService, UserService userService,
                            UserStatusRepository userStatusRepository,
                            WalletCategoryRepository walletCategoryRepository) {
        this.walletService = walletService;
        this.userService = userService;
        this.userStatusRepository = userStatusRepository;
        this.walletCategoryRepository = walletCategoryRepository;
    }

    @GetMapping("/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> one(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(walletService.getOne(wallet), HttpStatus.OK);
    }

    @GetMapping("/wallets")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(walletService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/balance")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> getWalletBalance(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        Map<Integer, BigDecimal> balanceMap = new HashMap<>();
        walletUserList.forEach(walletUser -> balanceMap.put(walletUser.getUser().getId(), walletUser.getBalance()));
        List<DebtsHolder> debtsList = new ArrayList<>();
        walletService.simplifyDebts(balanceMap, debtsList);

        return new ResponseEntity<>(debtsList, HttpStatus.OK);
    }

    @GetMapping("/wallet-users/{id}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> findsWalletUsers(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        List<Map<String, Object>> userList = walletService.findUserList(wallet);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/create-wallet")
    public ResponseEntity<?> createWallet(@Valid @RequestBody WalletHolder walletHolder) {
        walletService.save(walletHolder);

        return new ResponseEntity<>(walletHolder.getWallet(), HttpStatus.OK);
    }

    @PutMapping("/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody Map<String, String> map) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        updatedWallet.setName(map.get("name"));
        updatedWallet.setDescription(map.get("description"));

        WalletCategory walletCategory = walletCategoryRepository
                .findByName(map.get("walletCategory")).orElseThrow(RuntimeException::new);

        updatedWallet.setWalletCategory(walletCategory);

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @PutMapping("/wallet/{id}/users/{userLogin}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> addUsers(@PathVariable int id, @PathVariable String userLogin) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        UserStatus waitingStatus = userStatusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);

        walletService.saveUser(userLogin, updatedWallet, waitingStatus);

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @DeleteMapping("/wallet/{id}/user/{userLogin}")
    @PreAuthorize("@authenticationService.isWalletOwner(#id)")
    public ResponseEntity<?> deleteUserFromWallet(@PathVariable int id, @PathVariable String userLogin) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        User user = userService.findByLogin(userLogin).orElseThrow(RuntimeException::new);

        if (walletService.deleteUser(wallet, user))
            return new ResponseEntity<>("User has been deleted from the wallet!", HttpStatus.OK);
        else
            return new ResponseEntity<>("Cannot delete user from the wallet!", HttpStatus.CONFLICT);
    }

    @DeleteMapping("/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletOwner(#id)")
    public ResponseEntity<?> deleteOne(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        walletService.delete(wallet);

        return new ResponseEntity<>("Wallet has been deleted!", HttpStatus.OK);
    }
}
