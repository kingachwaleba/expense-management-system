package com.team.backend.repository;

import com.team.backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findById(int id);
    List<Wallet> findAll();

    @Query("from Wallet w inner join WalletUser wu on wu.wallet.id = w.id inner join User u on u.id= wu.user.id where u.id = ?1")
    List<Wallet> findByUserId(int id);
}
