package com.team.backend.repository;

import com.team.backend.model.WalletCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletCategoryRepository extends JpaRepository<WalletCategory, Integer> {
}