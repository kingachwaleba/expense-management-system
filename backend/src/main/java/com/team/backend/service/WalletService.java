package com.team.backend.service;

import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.Wallet;
import com.team.backend.model.WalletUser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WalletService {

    void save(WalletHolder walletHolder);

    Optional<Wallet> findById(int id);
    List<Wallet> findAll();

    List<Wallet> findByUserId(int id);
}