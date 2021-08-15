package com.team.backend.repository;

import com.team.backend.model.List;
import com.team.backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListRepository extends JpaRepository<List, Integer> {

    Optional<List> findById(int id);
    java.util.List<List> findAllByWallet(Wallet wallet);
}
