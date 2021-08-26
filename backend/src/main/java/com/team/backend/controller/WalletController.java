package com.team.backend.controller;

import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletCategoryRepository;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<?> one(@PathVariable int id) {
        Wallet wallet = walletService.findById(id)
                .orElseThrow(RuntimeException::new);

        Map<String, Object> map = new HashMap<>();

        map.put("walletId", wallet.getId());
        map.put("name", wallet.getName());
        map.put("walletCategory", wallet.getWalletCategory());
        map.put("description", wallet.getDescription());
        map.put("owner", walletService.findOwner(wallet).getLogin());
        map.put("userListCounter", walletService.findUserList(wallet).size());

        List<Map<String, Object>> userList = walletService.findUserList(wallet);
        map.put("userList", userList);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/wallets")
    public ResponseEntity<?> all() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);
        List<Wallet> wallets = walletService.findWallets(user);

        List<Map<String, Object>> walletsList = new ArrayList<>();

        for (Wallet wallet : wallets) {
            Map<String, Object> map = new HashMap<>();

            map.put("walletId", wallet.getId());
            map.put("name", wallet.getName());
            map.put("walletCategory", wallet.getWalletCategory());
            map.put("owner", walletService.findOwner(wallet).getLogin());
            map.put("userListCounter", walletService.findUserList(wallet).size());

            walletsList.add(map);
        }

        return new ResponseEntity<>(walletsList, HttpStatus.OK);
    }

    @GetMapping("/wallet-users/{id}")
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
    public ResponseEntity<?> addUsers(@PathVariable int id, @PathVariable String userLogin) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        UserStatus waitingStatus = userStatusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);

        walletService.saveUser(userLogin, updatedWallet, waitingStatus);

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }
}
