package com.team.backend.service;

import com.team.backend.helpers.DebtsHolder;
import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WalletService {

    void saveUser(String userLogin, Wallet wallet, UserStatus userStatus);
    void save(WalletHolder walletHolder);
    void save(Wallet wallet);
    void delete(Wallet wallet);
    boolean delete(WalletUser walletUser, Wallet wallet, User user);
    boolean deleteUser(Wallet wallet, User user);
    void leaveWallet(WalletUser walletUser, Wallet wallet, User user);

    Optional<Wallet> findById(int id);

    List<Wallet> findWallets(User user);

    List<Map<String, Object>> findUserList(Wallet wallet);
    List<Map<String, Object>> findAllUsers(Wallet wallet);
    List<WalletUser> findWalletUserList(Wallet wallet);
    List<String> findDeletedUserList(Wallet wallet);
    User findOwner(Wallet wallet);

    void simplifyDebts(Map<Integer, BigDecimal> balanceMap, List<DebtsHolder> debtsList);
    List<DebtsHolder> findDebts(Wallet wallet);

    Map<String, Object> getOne(Wallet wallet);
    List<Map<String, Object>> getAll();

    Map<String, Object> returnStats(Wallet wallet, LocalDateTime from, LocalDateTime to);

    List<String> getErrorList(BindingResult bindingResult);

}