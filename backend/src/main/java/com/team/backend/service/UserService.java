package com.team.backend.service;

import com.team.backend.model.User;

import java.util.Optional;

public interface UserService {

    void save(User user);
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);

    Boolean checkIfValidOldPassword(User user, String oldPassword);
    void changeUserPassword(User user, String password);
}