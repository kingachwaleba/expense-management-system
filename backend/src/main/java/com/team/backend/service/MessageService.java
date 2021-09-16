package com.team.backend.service;

import com.team.backend.model.Message;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageService {

    void save(Message message, Wallet wallet, User user);
    void saveNotifications(Wallet wallet, User user, User messageSender, String content, String type);

    void delete(Message message);

    List<Message> findAllByWalletAndTypeOrderByDate(Wallet wallet, String type);
    List<Message> findAllByReceiverAndTypeOrderByDate(User user, String type);

    Optional<Message> findById(Integer id);
}
