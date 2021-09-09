package com.team.backend.repository;

import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.WalletUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface WalletUserRepository extends JpaRepository<WalletUser, Integer> {

    Set<WalletUser> findAllByUserStatusAndUser(UserStatus userStatus, User user);
    Optional<WalletUser> findById(int id);
}