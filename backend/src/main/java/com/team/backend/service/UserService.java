package com.team.backend.service;

import com.team.backend.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    void save(User user);
    void saveDeleted(User user);
    boolean ifAccountDeleted(User user);
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    List<User> findByDeletedAndLoginContaining(String deleted, String infix);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);

    Boolean checkIfValidOldPassword(User user, String oldPassword);
    void changeUserPassword(User user, String password);
    void changeUserImage(User user, String imageUrl);

    Optional<User> findCurrentLoggedInUser();

    BigDecimal calculateUserBalance(User user);

    List<String> findUserForWallet(int id, String infix);
    List<String> findUser(String infix);
    Map<String, String> findUserDetails();
}