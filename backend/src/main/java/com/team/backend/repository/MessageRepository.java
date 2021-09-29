package com.team.backend.repository;

import com.team.backend.model.Message;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByWalletAndTypeOrderByDate(Wallet wallet, String type);
    List<Message> findAllByReceiverAndTypeOrderByDate(User user, String type);
    List<Message> findAllByWallet(Wallet wallet);
    List<Message> findAllByReceiver(User user);
    Optional<Message> findById(Integer id);
    void deleteAllByWallet(Wallet wallet);
}
