package com.team.backend.service;

import com.team.backend.model.Message;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import com.team.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private WalletService walletService;

    public MessageServiceImpl(MessageRepository messageRepository, WalletService walletService) {
        this.messageRepository = messageRepository;
        this.walletService = walletService;
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
    public void saveNotifications(Wallet wallet, User user, User messageSender) {
        Message message = new Message();

        message.setReceiver(user);
        message.setSender(messageSender);
        message.setWallet(wallet);
        message.setContent("Nowa wiadomość");
        message.setDate(LocalDateTime.now());
        message.setType("S");

        messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public List<Map<String, Object>> findAllMessages(Wallet wallet, String type) {
        List<Message> messageList = messageRepository.findAllByWalletAndTypeOrderByDate(wallet, type);

        List<Map<String, Object>> messages = new ArrayList<>();

        for (Message message : messageList) {
            Map<String, Object> map = new HashMap<>();

            map.put("walletId", message.getWallet().getId());
            map.put("senderLogin", message.getSender().getLogin());
            map.put("date", message.getDate());
            map.put("content", message.getContent());

            messages.add(map);
        }

        return messages;
    }

    @Override
    public List<Map<String, Object>> findAllNotifications(User user, String type) {
        List<Message> messageList = messageRepository.findAllByReceiverAndTypeOrderByDate(user, type);

        List<Map<String, Object>> notifications = new ArrayList<>();

        for (Message message : messageList) {
            Map<String, Object> map = new HashMap<>();

            map.put("messageId", message.getId());
            map.put("walletId", message.getWallet().getId());
            map.put("walletName", message.getWallet().getName());
            if (message.getSender() != null)
                map.put("senderLogin", message.getSender().getLogin());
            else
                map.put("senderLogin", null);
            map.put("receiverId", message.getReceiver().getId());
            map.put("date", message.getDate());
            map.put("content", message.getContent());
            map.put("walletOwnerLogin", walletService.findOwner(message.getWallet()).getLogin());

            notifications.add(map);
        }

        return notifications;
    }

    @Override
    public Optional<Message> findById(Integer id) {
        return messageRepository.findById(id);
    }
}
