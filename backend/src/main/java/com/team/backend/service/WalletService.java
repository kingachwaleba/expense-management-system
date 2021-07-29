package com.team.backend.service;

import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.Wallet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalletService {

//    void saveUsers(List<User> userList, Wallet wallet);
    void saveUser(String userLogin, Wallet wallet, UserStatus userStatus);
    void save(WalletHolder walletHolder);
    void save(Wallet wallet);

    Optional<Wallet> findById(int id);
    List<Wallet> findAll();

    List<Wallet> findByUserId(int id);
}