package com.team.backend.controller;

import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.Wallet;
import com.team.backend.model.WalletUser;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;
    private final UserStatusRepository userStatusRepository;

    public WalletController(WalletService walletService, UserService userService, UserStatusRepository userStatusRepository) {
        this.walletService = walletService;
        this.userService = userService;
        this.userStatusRepository = userStatusRepository;
    }

    @GetMapping("/wallet/{id}")
    public ResponseEntity<Wallet> one(@PathVariable int id) {
        Wallet wallet = walletService.findById(id)
                .orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @GetMapping("/wallets")
    public ResponseEntity<List<Wallet>> all() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        User user = userService.findByLogin(currentUserLogin).orElseThrow(RuntimeException::new);

        List<Wallet> walletList = walletService.findByUserId(user.getId());

        return new ResponseEntity<>(walletList, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/create-wallet")
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody WalletHolder walletHolder) {
        walletService.save(walletHolder);

        return new ResponseEntity<>(walletHolder.getWallet(), HttpStatus.OK);
    }

    @PutMapping("/wallet/{id}")
    public ResponseEntity<Wallet> editOne(@PathVariable int id, @RequestBody Wallet newWallet) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        updatedWallet.setName(newWallet.getName());
        updatedWallet.setDescription(newWallet.getDescription());
        updatedWallet.setWalletCategory(newWallet.getWalletCategory());

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @PutMapping("/wallet/{id}/users")
    public ResponseEntity<?> addUsers(@PathVariable int id, @RequestBody User user) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        for (WalletUser walletUser : updatedWallet.getWalletUserSet()) {
            if (walletUser.getUser().getId() == user.getId())
                return new ResponseEntity<>("Person already exists for login " + user.getLogin() + " in this wallet!", HttpStatus.CONFLICT);
        }

        // Get user status for others wallets' members
        UserStatus waitingStatus = userStatusRepository.findById(2).orElseThrow(RuntimeException::new);

        walletService.saveUser(user, updatedWallet, waitingStatus);

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }
}
