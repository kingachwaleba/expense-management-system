package com.team.backend.service;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.List;
import com.team.backend.model.Status;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;

import java.util.Optional;

public interface ListService {

    void save(ListHolder listHolder, Wallet wallet);
    void save(List list);
    void delete(List list);

    Optional<List> findById(int id);
    java.util.List<List> findAllByWallet(Wallet wallet);
    java.util.List<List> findAllByUserAndWalletAndStatus(User user, Wallet wallet, Status status);
}
