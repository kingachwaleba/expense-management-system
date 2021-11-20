package com.team.backend.controller;

import com.team.backend.config.ErrorMessage;
import com.team.backend.exception.MessageNotFoundException;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.exception.WalletNotFoundException;
import com.team.backend.exception.WalletUserNotFoundException;
import com.team.backend.helpers.DebtsHolder;
import com.team.backend.model.Message;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import com.team.backend.model.WalletUser;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.MessageService;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MessageController {

    private final WalletService walletService;
    private final UserService userService;
    private final MessageService messageService;
    private final WalletUserRepository walletUserRepository;
    private final ErrorMessage errorMessage;

    public MessageController(WalletService walletService, UserService userService, MessageService messageService,
                             WalletUserRepository walletUserRepository, ErrorMessage errorMessage) {
        this.walletService = walletService;
        this.userService = userService;
        this.messageService = messageService;
        this.walletUserRepository = walletUserRepository;
        this.errorMessage = errorMessage;
    }

    @GetMapping("/wallet/{id}/message/{stringDate}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> allMessages(@PathVariable int id, @PathVariable String stringDate) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        LocalDateTime date;
        try {
            date = LocalDateTime.parse(stringDate);
        } catch (DateTimeException dateTimeException) {
            return new ResponseEntity<>(dateTimeException.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<Message> messageList =
                messageService.findAllByWalletAndTypeOrderByDate(wallet, String.valueOf(Message.MessageType.M));

        messageList = messageList.stream().filter(
                message -> message.getDate().isBefore(date)).collect(Collectors.toList());

        if (messageList.size() > 10)
            messageList = messageList.subList(messageList.size() - 10, messageList.size());

        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        messageService.removeNotifications(user, wallet, String.valueOf(Message.MessageType.R));
        messageService.manageMessageNotifications(user);

        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }

    @GetMapping("/debts-notifications")
    public ResponseEntity<?> debtsNotifications() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        List<Message> debtsList =
                messageService.findAllByReceiverAndTypeOrderByDate(user, String.valueOf(Message.MessageType.E));
        debtsList.addAll(messageService
                .findAllByReceiverAndTypeOrderByDate(user, String.valueOf(Message.MessageType.S)));

        return new ResponseEntity<>(debtsList, HttpStatus.OK);
    }

    @GetMapping("/message-notifications")
    public ResponseEntity<?> messageNotifications() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        List<Message> messageNotifications = messageService.manageMessageNotifications(user);

        return new ResponseEntity<>(messageNotifications, HttpStatus.OK);
    }

    @PostMapping("/wallet/{id}/message")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> createMessage(@PathVariable int id, @Valid @RequestBody Message message,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(errorMessage.get("message.error.content"), HttpStatus.BAD_REQUEST);

        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        messageService.save(message, wallet, user);

        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        walletUserList = walletUserList.stream().filter(
                walletUser -> !walletUser.getUser().equals(user)).collect(Collectors.toList());
        messageService.removeNotifications(user, wallet, String.valueOf(Message.MessageType.R));
        messageService.manageMessageNotifications(user);
        walletUserList.forEach(walletUser ->
                messageService.saveNotifications(
                        wallet,
                        walletUser.getUser(),
                        null,
                        "Nowa wiadomość",
                        String.valueOf(Message.MessageType.R)
                )
        );

        return ResponseEntity.ok("New message has been sent!");
    }

    @PostMapping("/send-notification/wallet/{id}")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> sendDebtNotification(@PathVariable int id, @RequestBody DebtsHolder debtsHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);
        User debtor = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getDebtor()).orElseThrow(WalletUserNotFoundException::new)
                .getUser();
        User creditor = walletUserRepository
                .findByWalletAndUser(wallet, debtsHolder.getCreditor()).orElseThrow(WalletUserNotFoundException::new)
                .getUser();
        BigDecimal debt = debtsHolder.getHowMuch();
        String content = String.valueOf(debt);

        messageService.saveNotifications(wallet, debtor, creditor, content, String.valueOf(Message.MessageType.E));

        return new ResponseEntity<>("The notification has been sent!", HttpStatus.OK);
    }

    @DeleteMapping("/notifications/{id}")
    @PreAuthorize("@authenticationService.isNotificationOwner(#id)")
    public ResponseEntity<?> deleteNotification(@PathVariable int id) {
        Message notification = messageService.findById(id).orElseThrow(MessageNotFoundException::new);
        messageService.delete(notification);

        return ResponseEntity.ok("Notification has been deleted!");
    }

    @DeleteMapping("/notifications/{id}/messages")
    @PreAuthorize("@authenticationService.isNotificationOwner(#id)")
    public ResponseEntity<?> deleteMessageNotification(@PathVariable int id) {
        Message notification = messageService.findById(id).orElseThrow(MessageNotFoundException::new);
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        Map<Wallet, List<Message>> walletNotificationsMap = messageService.findWalletNotificationsMap(user);
        List<Message> messageList = walletNotificationsMap.get(notification.getWallet());

        messageList.forEach(messageService::delete);

        return ResponseEntity.ok("Notifications have been deleted!");
    }
}
