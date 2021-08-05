package com.team.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.Wallet;
import com.team.backend.model.WalletUser;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class WalletUserController {

    private final UserService userService;
    private final UserStatusRepository userStatusRepository;
    private final WalletUserRepository walletUserRepository;
    private final WalletService walletService;
    private final ObjectMapper objectMapper;

    public WalletUserController(UserService userService, UserStatusRepository userStatusRepository,
                                WalletUserRepository walletUserRepository, WalletService walletService,
                                ObjectMapper objectMapper) {
        this.userService = userService;
        this.userStatusRepository = userStatusRepository;
        this.walletUserRepository = walletUserRepository;
        this.walletService = walletService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/notifications/invitations")
    public ResponseEntity<?> all() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        User user = userService.findByLogin(currentUserLogin).orElseThrow(RuntimeException::new);

        // Get user status for the waiting status
        UserStatus waitingStatus = userStatusRepository.findById(2).orElseThrow(RuntimeException::new);

        Set<WalletUser> invitations = walletUserRepository.findAllByUserStatusAndUser(waitingStatus, user);

        List<ObjectNode> invitationsList = new ArrayList<>();

        for (WalletUser walletUser : invitations) {
            ObjectNode objectNode = objectMapper.createObjectNode();

            objectNode.put("walletId", walletUser.getWallet().getId());
            objectNode.put("walletUserId", walletUser.getId());
            objectNode.put("name", walletUser.getWallet().getName());
            objectNode.put("owner", walletService.findOwner(walletUser.getWallet()).getLogin());
            objectNode.put("userListCounter", walletService.findUserList(walletUser.getWallet()).size());

            invitationsList.add(objectNode);
        }

        return new ResponseEntity<>(invitationsList, HttpStatus.OK);
    }

    @PutMapping("/notifications/invitations/{id}")
    public ResponseEntity<?> manageInvitations(@PathVariable int id, @RequestBody boolean flag) {
        WalletUser updatedWalletUser = walletUserRepository.findById(id).orElseThrow(RuntimeException::new);

        if (flag) {
            UserStatus memberStatus = userStatusRepository.findById(4).orElseThrow(RuntimeException::new);
            LocalDateTime date = LocalDateTime.now();
            updatedWalletUser.setAccepted_at(date);
            updatedWalletUser.setUserStatus(memberStatus);
        }
        else {
            UserStatus cancelledStatus = userStatusRepository.findById(3).orElseThrow(RuntimeException::new);
            updatedWalletUser.setUserStatus(cancelledStatus);
        }

        walletUserRepository.save(updatedWalletUser);

        return new ResponseEntity<>("User status has been changed!", HttpStatus.OK);
    }
}
