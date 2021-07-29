package com.team.backend.service;

import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import com.team.backend.repository.UserRepository;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private UserStatusRepository userStatusRepository;
    private WalletRepository walletRepository;
    private UserRepository userRepository;

    public WalletServiceImpl(UserStatusRepository userStatusRepository, WalletRepository walletRepository, UserRepository userRepository) {
        this.userStatusRepository = userStatusRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(String userLogin, Wallet wallet, UserStatus userStatus) {
        User user = userRepository.findByLogin(userLogin).orElseThrow(RuntimeException::new);

        LocalDateTime date = LocalDateTime.now();

        WalletUser walletUser = new WalletUser();
        walletUser.setUser(user);
        walletUser.setCreated_at(date);

        if (userStatus.getId() == 1)
            walletUser.setAccepted_at(date);
        else
            walletUser.setAccepted_at(null);

        walletUser.setUserStatus(userStatus);
        wallet.addWalletUser(walletUser);
    }

    @Override
    public void save(WalletHolder walletHolder) {
        Wallet wallet = walletHolder.getWallet();
        List<String> userLoginList = walletHolder.getUserList();

        // Get current logged in user and set it
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        // Get user status for wallet's owner
        UserStatus ownerStatus = userStatusRepository.findById(1).orElseThrow(RuntimeException::new);

        // Get user status for others wallets' members
        UserStatus waitingStatus = userStatusRepository.findById(2).orElseThrow(RuntimeException::new);

        // Save the wallet's owner
        saveUser(currentUserLogin, wallet, ownerStatus);

        // Save others wallet's members
        for (String memberLogin : userLoginList) {
            saveUser(memberLogin, wallet, waitingStatus);
        }

        walletRepository.save(wallet);
    }

    @Override
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> findById(int id) {
        return walletRepository.findById(id);
    }

    @Override
    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    @Override
    public List<Wallet> findByUserId(int id) {
        return walletRepository.findByUserId(id);
    }
}
