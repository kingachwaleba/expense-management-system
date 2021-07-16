package com.team.backend.service;

import com.team.backend.model.User;

import java.util.Optional;

public interface UserService {

    void save(User user);
    Optional<User> findByLogin(String login);
}