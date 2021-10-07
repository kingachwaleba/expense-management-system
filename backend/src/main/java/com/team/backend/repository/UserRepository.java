package com.team.backend.repository;

import com.team.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
    Optional<User> findById(int id);
    List<User> findByDeletedAndLoginContaining(String deleted, String infix);
    Boolean existsByLogin(String login);
    Boolean existsByEmailAndDeleted(String email, String deleted);
}