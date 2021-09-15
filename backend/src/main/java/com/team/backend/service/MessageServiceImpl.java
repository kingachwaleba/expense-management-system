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

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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
    public List<Message> findAllByWalletAndTypeOrderByDate(Wallet wallet, String type) {
        return messageRepository.findAllByWalletAndTypeOrderByDate(wallet, type);
    }

    @Override
    public List<Message> findAllByReceiverAndTypeOrderByDate(User user, String type) {
        return messageRepository.findAllByReceiverAndTypeOrderByDate(user, type);
    }

    @Override
    public Optional<Message> findById(Integer id) {
        return messageRepository.findById(id);
    }
}
