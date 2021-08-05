package com.team.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.WalletUser;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
