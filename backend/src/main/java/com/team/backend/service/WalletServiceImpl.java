package com.team.backend.service;

import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import com.team.backend.repository.UserRepository;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public void save(WalletHolder walletHolder) {
        Wallet wallet = walletHolder.getWallet();
        List<User> userList= walletHolder.getUserList();

        WalletUser walletUser = new WalletUser();

        // Get current logged in user and set it
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        User user = userRepository.findByLogin(currentUserLogin).orElseThrow(RuntimeException::new);

        Date date = new Date();

        // Get user status for wallet's owner
        UserStatus userStatus = userStatusRepository.findById(1).orElseThrow(RuntimeException::new);

        // Get user status for others wallets' members
        UserStatus userStatus2 = userStatusRepository.findById(2).orElseThrow(RuntimeException::new);

        for (User user1 : userList) {
            WalletUser walletUser1 = new WalletUser();
            walletUser1.setUser(user1);
            walletUser1.setCreated_at(date);
            walletUser1.setAccepted_at(date);
            walletUser1.setUserStatus(userStatus2);
            wallet.addWalletUser(walletUser1);
        }

        walletUser.setUser(user);
        walletUser.setCreated_at(date);
        walletUser.setAccepted_at(date);
        walletUser.setUserStatus(userStatus);

        wallet.addWalletUser(walletUser);
        walletRepository.save(wallet);
    }
}
