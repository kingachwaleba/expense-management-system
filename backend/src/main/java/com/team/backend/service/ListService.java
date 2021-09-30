package com.team.backend.service;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.ShoppingList;
import com.team.backend.model.Status;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;

import java.util.List;
import java.util.Optional;

public interface ListService {

    void save(ListHolder listHolder, Wallet wallet);
    void save(ShoppingList list);
    void delete(ShoppingList list);
    void deleteAllByWallet(Wallet wallet);

    Optional<ShoppingList> findById(int id);
    List<ShoppingList> findAllByWallet(Wallet wallet);
    List<ShoppingList> findAllByUserAndWalletAndStatus(User user, Wallet wallet, Status status);
}
