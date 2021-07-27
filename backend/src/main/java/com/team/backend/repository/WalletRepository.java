package com.team.backend.repository;

import com.team.backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findById(int id);
    List<Wallet> findAll();
}
