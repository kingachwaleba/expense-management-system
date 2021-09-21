package com.team.backend.service;

import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.Wallet;
import com.team.backend.model.WalletUser;
import com.team.backend.repository.UserStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final WalletService walletService;
    private final UserService userService;
    private final UserStatusRepository userStatusRepository;

    public AuthenticationService(WalletService walletService, UserService userService, UserStatusRepository userStatusRepository) {
        this.walletService = walletService;
        this.userService = userService;
        this.userStatusRepository = userStatusRepository;
    }

    public boolean isWalletOwner(int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        User currentUser = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);
        UserStatus ownerStatus = userStatusRepository.findByName("właściciel").orElseThrow(RuntimeException::new);
        WalletUser walletOwnerDetail = wallet.getWalletUserSet().stream()
                .filter(
                        walletUser -> walletUser.getUserStatus().equals(ownerStatus)
                )
                .findFirst().orElseThrow(RuntimeException::new);
        User owner = walletOwnerDetail.getUser();

        return owner.equals(currentUser);
    }
}
