package com.team.backend.controller;

import com.team.backend.helpers.DebtsHolder;
import com.team.backend.model.Message;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import com.team.backend.repository.MessageRepository;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.MessageService;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController {

    private final WalletService walletService;
    private final UserService userService;
    private final MessageService messageService;
    private final WalletUserRepository walletUserRepository;

    public MessageController(WalletService walletService, UserService userService, MessageService messageService,
                             WalletUserRepository walletUserRepository) {
        this.walletService = walletService;
        this.userService = userService;
        this.messageService = messageService;
        this.walletUserRepository = walletUserRepository;
    }

    @GetMapping("/wallet/{id}/message")
    public ResponseEntity<?> all(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(messageService.findAllByWalletAndTypeOrderByDate(wallet, "M"), HttpStatus.OK);
    }

    @GetMapping("/debts-notifications")
    public ResponseEntity<?> allDebtsNotifications() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(messageService.findAllByReceiverAndTypeOrderByDate(user, "E"), HttpStatus.OK);
    }

    @PostMapping("/wallet/{id}/message")
    public ResponseEntity<?> createMessage(@PathVariable int id, @Valid @RequestBody Message message) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        messageService.save(message, wallet, user);

        List<Map<String, Object>> userList = walletService.findUserList(wallet);
        for (Map<String, Object> mapUser : userList) {
            User user2 = userService.findByLogin(mapUser.get("login").toString()).orElseThrow(RuntimeException::new);

//            if (user2.getId() != user.getId())
//                messageService.saveNotifications(wallet, user2, user);
        }

        return ResponseEntity.ok("New message has been sent!");
    }

    @PostMapping("/send-notification/wallet/{id}")
    public ResponseEntity<?> sendDebtNotification(@PathVariable int id, @RequestBody DebtsHolder debtsHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        User debtor = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getDebtor()).orElseThrow(RuntimeException::new).getUser();
        User creditor = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getCreditor()).orElseThrow(RuntimeException::new).getUser();
        BigDecimal debt = debtsHolder.getHowMuch();
        String content = String.valueOf(debt);

        messageService.saveNotifications(wallet, debtor, creditor, content, String.valueOf(Message.MessageType.E));

        return new ResponseEntity<>("The notification has been sent!", HttpStatus.OK);
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable int id) {
        Message notification = messageService.findById(id).orElseThrow(RuntimeException::new);
        messageService.delete(notification);

        return ResponseEntity.ok("Notification has been deleted!");
    }
}
