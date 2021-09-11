package com.team.backend.controller;

import com.team.backend.helpers.DebtsHolder;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);
        UserStatus waitingStatus = userStatusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);
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
    public ResponseEntity<?> manageInvitations(@PathVariable int id, @RequestBody boolean flag) {
        WalletUser updatedWalletUser = walletUserRepository.findById(id).orElseThrow(RuntimeException::new);

        if (flag) {
            UserStatus memberStatus = userStatusRepository.findByName("członek").orElseThrow(RuntimeException::new);
            LocalDateTime date = LocalDateTime.now();
            updatedWalletUser.setAccepted_at(date);
            updatedWalletUser.setUserStatus(memberStatus);
        }
        else {
            UserStatus cancelledStatus = userStatusRepository.findByName("odrzucony").orElseThrow(RuntimeException::new);
            updatedWalletUser.setUserStatus(cancelledStatus);
        }

        walletUserRepository.save(updatedWalletUser);

        return new ResponseEntity<>("User status has been changed!", HttpStatus.OK);
    }

    @PutMapping("/pay-debt/wallet/{id}")
    public ResponseEntity<?> payDebt(@PathVariable int id, @RequestBody DebtsHolder debtsHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        WalletUser debtorInfo = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getDebtor()).orElseThrow(RuntimeException::new);
        WalletUser creditorInfo = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getCreditor()).orElseThrow(RuntimeException::new);

        BigDecimal debt = debtsHolder.getHowMuch();
        BigDecimal newDebtorBalance = debtorInfo.getBalance().add(debt);
        BigDecimal newCreditorBalance = creditorInfo.getBalance().subtract(debt);

        if (newDebtorBalance.abs().compareTo(BigDecimal.valueOf(0.10)) < 0)
            newDebtorBalance = BigDecimal.valueOf(0.00);
        if (newCreditorBalance.abs().compareTo(BigDecimal.valueOf(0.10)) < 0)
            newCreditorBalance = BigDecimal.valueOf(0.00);

        debtorInfo.setBalance(newDebtorBalance);
        creditorInfo.setBalance(newCreditorBalance);

        walletUserRepository.save(debtorInfo);
        walletUserRepository.save(creditorInfo);

        return new ResponseEntity<>("The debt has been paid!", HttpStatus.OK);
    }
}
