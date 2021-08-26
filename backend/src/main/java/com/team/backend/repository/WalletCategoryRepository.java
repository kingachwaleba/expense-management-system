package com.team.backend.repository;

import com.team.backend.model.WalletCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletCategoryRepository extends JpaRepository<WalletCategory, Integer> {

    Optional<WalletCategory> findByName(String name);
}