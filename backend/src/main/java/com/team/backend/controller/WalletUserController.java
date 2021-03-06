package com.team.backend.controller;

import com.team.backend.exception.UserNotFoundException;
import com.team.backend.exception.UserStatusNotFoundException;
import com.team.backend.exception.WalletUserNotFoundException;
import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.WalletUser;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class WalletUserController {

    private final UserService userService;
    private final UserStatusRepository userStatusRepository;
    private final WalletUserRepository walletUserRepository;
    private final WalletService walletService;

    public WalletUserController(UserService userService, UserStatusRepository userStatusRepository,
                                WalletUserRepository walletUserRepository, WalletService walletService) {
        this.userService = userService;
        this.userStatusRepository = userStatusRepository;
        this.walletUserRepository = walletUserRepository;
        this.walletService = walletService;
    }

    @GetMapping("/notifications/invitations")
    public ResponseEntity<?> all() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        UserStatus waitingStatus = userStatusRepository.findByName("oczekujący")
                .orElseThrow(UserStatusNotFoundException::new);
        Set<WalletUser> invitations = walletUserRepository.findAllByUserStatusAndUser(waitingStatus, user);
        List<Map<String, Object>> invitationsList = new ArrayList<>();

        for (WalletUser walletUser : invitations) {
            Map<String, Object> map = new HashMap<>();

            map.put("walletId", walletUser.getWallet().getId());
            map.put("walletUserId", walletUser.getId());
            map.put("name", walletUser.getWallet().getName());
            map.put("owner", walletService.findOwner(walletUser.getWallet()).getLogin());
            map.put("userListCounter", walletService.findUserList(walletUser.getWallet()).size());

            invitationsList.add(map);
        }

        return new ResponseEntity<>(invitationsList, HttpStatus.OK);
    }

    @PutMapping("/notifications/invitations/{id}")
    @PreAuthorize("@authenticationService.isInvitationOwner(#id)")
    public ResponseEntity<?> manageInvitations(@PathVariable int id, @RequestBody boolean flag) {
        WalletUser updatedWalletUser = walletUserRepository.findById(id).orElseThrow(WalletUserNotFoundException::new);

        if (flag) {
            UserStatus memberStatus = userStatusRepository.findByName("członek")
                    .orElseThrow(UserStatusNotFoundException::new);
            LocalDateTime date = LocalDateTime.now();
            updatedWalletUser.setAccepted_at(date);
            updatedWalletUser.setUserStatus(memberStatus);

            walletUserRepository.save(updatedWalletUser);
        }
        else 
            walletUserRepository.delete(updatedWalletUser);

        return new ResponseEntity<>("User status has been changed!", HttpStatus.OK);
    }
}
