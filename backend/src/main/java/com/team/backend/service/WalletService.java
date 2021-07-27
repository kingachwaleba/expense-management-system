package com.team.backend.service;

import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletService {

    void save(WalletHolder walletHolder);

    Optional<Wallet> findById(int id);
    List<Wallet> findAll();
}