package com.team.backend.repository;

import com.team.backend.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {

    Optional<UserStatus> findByName(String name);
}