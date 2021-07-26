package com.team.backend.repository;

import com.team.backend.model.WalletUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletUserRepository extends JpaRepository<WalletUser, Integer> {
}