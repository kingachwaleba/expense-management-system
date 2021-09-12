package com.team.backend.service;

import com.team.backend.helpers.DebtsHolder;
import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.User;
import com.team.backend.model.UserStatus;
import com.team.backend.model.Wallet;
import com.team.backend.model.WalletUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WalletService {

    void saveUser(String userLogin, Wallet wallet, UserStatus userStatus);
    void save(WalletHolder walletHolder);
    void save(Wallet wallet);

    Optional<Wallet> findById(int id);

    List<Wallet> findWallets(User user);

    List<Map<String, Object>> findUserList(Wallet wallet);
    List<Map<String, Object>> findAllUsers(Wallet wallet);
    List<WalletUser> findWalletUserList(Wallet wallet);
    User findOwner(Wallet wallet);

    void simplifyDebts(Map<Integer, BigDecimal> balanceMap, List<DebtsHolder> debtsList);
}