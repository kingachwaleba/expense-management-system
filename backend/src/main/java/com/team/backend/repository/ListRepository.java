package com.team.backend.repository;

import com.team.backend.model.ShoppingList;
import com.team.backend.model.Status;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListRepository extends JpaRepository<ShoppingList, Integer> {

    Optional<ShoppingList> findById(int id);
    List<ShoppingList> findAllByWallet(Wallet wallet);
    List<ShoppingList> findAllByUserAndWalletAndStatus(User user, Wallet wallet, Status status);
    void deleteAllByWallet(Wallet wallet);
}
