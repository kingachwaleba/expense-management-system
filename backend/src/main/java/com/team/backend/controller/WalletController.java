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

    public WalletController(WalletService walletService, UserService userService,
                            UserStatusRepository userStatusRepository) {
        this.walletService = walletService;
        this.userService = userService;
        this.userStatusRepository = userStatusRepository;
    }

    @GetMapping("/wallet/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {
        Wallet wallet = walletService.findById(id)
                .orElseThrow(RuntimeException::new);

        Map<String, Object> map = new HashMap<>();

        map.put("walletId", wallet.getId());
        map.put("name", wallet.getName());
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
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody Wallet newWallet) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        updatedWallet.setName(newWallet.getName());
        updatedWallet.setDescription(newWallet.getDescription());
        updatedWallet.setWalletCategory(newWallet.getWalletCategory());

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @PutMapping("/wallet/{id}/users/{userLogin}")
    public ResponseEntity<?> addUsers(@PathVariable int id, @PathVariable String userLogin) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        for (WalletUser walletUser : updatedWallet.getWalletUserSet()) {
            if (walletUser.getUser().getLogin().equals(userLogin))
                return new ResponseEntity<>("Person already exists for login " + userLogin + " in this wallet!", HttpStatus.CONFLICT);
        }

        UserStatus waitingStatus = userStatusRepository.findById(2).orElseThrow(RuntimeException::new);

        walletService.saveUser(userLogin, updatedWallet, waitingStatus);

        walletService.save(updatedWallet);

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }
}
