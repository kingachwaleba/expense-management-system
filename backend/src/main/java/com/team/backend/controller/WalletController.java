package com.team.backend.controller;

import com.team.backend.model.*;
import com.team.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
public class WalletController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final UserStatusRepository userStatusRepository;
    private final WalletCategoryRepository walletCategoryRepository;

    public WalletController(UserRepository userRepository, WalletRepository walletRepository,
                            UserStatusRepository userStatusRepository, WalletCategoryRepository walletCategoryRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.userStatusRepository = userStatusRepository;
        this.walletCategoryRepository = walletCategoryRepository;
    }

    @Transactional
    @PostMapping("/create-wallet")
    public ResponseEntity<String> createWallet(@Valid @RequestBody Wallet wallet) throws UsernameNotFoundException {
        WalletUser walletUser = new WalletUser();

        // Get current logged in user and set it
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUserLogin = authentication.getName();

//        Optional<User> optionalUser = userRepository.findByLogin(currentUserLogin);
//        if (optionalUser.isEmpty()) throw new UsernameNotFoundException(currentUserLogin);
//        User user = optionalUser.get();

        Optional<User> optionalUser = userRepository.findByLogin("Ala12345");
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("Ala12345");
        User user = optionalUser.get();

        walletUser.setUser(user);

        // Get current date and set it
        Date date = new Date();
        walletUser.setCreated_at(date);
        walletUser.setAccepted_at(date);

        Optional<UserStatus> optionalUserStatus = userStatusRepository.findById(1);
        optionalUserStatus.ifPresent(walletUser::setUserStatus);

        Optional<WalletCategory> optionalWalletCategory = walletCategoryRepository.findById(1);
        optionalWalletCategory.ifPresent(wallet::setWalletCategory);

        wallet.addWalletUser(walletUser);
        walletRepository.save(wallet);

        return ResponseEntity.ok("Wallet has been created");
    }
}
