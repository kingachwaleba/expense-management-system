package com.team.backend.service;

import com.team.backend.exception.WalletNotFoundException;
import com.team.backend.helpers.DebtsHolder;
import com.team.backend.model.Message;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import com.team.backend.repository.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final WalletService walletService;

    public MessageServiceImpl(MessageRepository messageRepository, @Lazy WalletService walletService) {
        this.messageRepository = messageRepository;
        this.walletService = walletService;
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void save(Message message, Wallet wallet, User user) {
        LocalDateTime date = LocalDateTime.now();

        message.setDate(date);
        message.setType("M");
        message.setSender(user);
        message.setReceiver(null);
        message.setWallet(wallet);

        messageRepository.save(message);
    }

    @Override
    public void saveNotifications(Wallet wallet, User user, User messageSender, String content, String type) {
        Message message = new Message();

        message.setReceiver(user);
        message.setSender(messageSender);
        message.setWallet(wallet);
        message.setContent(content);
        message.setDate(LocalDateTime.now());
        message.setType(type);

        messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public void deleteAllByWallet(Wallet wallet) {
        messageRepository.deleteAllByWallet(wallet);
    }

    @Override
    public List<Message> findAllByWalletAndTypeOrderByDate(Wallet wallet, String type) {
        return messageRepository.findAllByWalletAndTypeOrderByDate(wallet, type);
    }

    @Override
    public List<Message> findAllByReceiverAndTypeOrderByDate(User user, String type) {
        return messageRepository.findAllByReceiverAndTypeOrderByDate(user, type);
    }

    @Override
    public List<Message> findAllByWallet(Wallet wallet) {
        return messageRepository.findAllByWallet(wallet);
    }

    @Override
    public List<Message> findAllByReceiver(User user) {
        return messageRepository.findAllByReceiver(user);
    }

    @Override
    public Optional<Message> findById(Integer id) {
        return messageRepository.findById(id);
    }

    @Override
    public void sendNotification(int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);
        List<Message> systemNotificationsList =
                findAllByWalletAndTypeOrderByDate(wallet, String.valueOf(Message.MessageType.S));
        List<Message> userNotificationsList =
                findAllByWalletAndTypeOrderByDate(wallet, String.valueOf(Message.MessageType.E));
        List<Message> notificationsList = new ArrayList<>();
        notificationsList.addAll(systemNotificationsList);
        notificationsList.addAll(userNotificationsList);
        notificationsList.forEach(this::delete);

        for (DebtsHolder debtsHolder : walletService.findDebts(wallet)) {
            Message message = new Message(
                    debtsHolder.getDebtor(),
                    wallet,
                    LocalDateTime.now(),
                    String.valueOf(debtsHolder.getHowMuch()),
                    String.valueOf(Message.MessageType.S),
                    null
            );

            save(message);
        }
    }
}
