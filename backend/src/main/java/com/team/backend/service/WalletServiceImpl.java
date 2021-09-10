package com.team.backend.service;

import com.team.backend.helpers.DebtsHolder;
import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import com.team.backend.repository.UserStatusRepository;
import com.team.backend.repository.WalletRepository;
import com.team.backend.repository.WalletUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserStatusRepository userStatusRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final WalletUserRepository walletUserRepository;

    public WalletServiceImpl(UserStatusRepository userStatusRepository, WalletRepository walletRepository,
                             UserService userService,
                             WalletUserRepository walletUserRepository) {
        this.userStatusRepository = userStatusRepository;
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.walletUserRepository = walletUserRepository;
    }

    @Override
    public void saveUser(String userLogin, Wallet wallet, UserStatus userStatus) {
        User user = userService.findByLogin(userLogin).orElseThrow(RuntimeException::new);

        LocalDateTime date = LocalDateTime.now();

        WalletUser walletUser = new WalletUser();
        walletUser.setUser(user);
        walletUser.setCreated_at(date);
        walletUser.setBalance(BigDecimal.valueOf(0.00));

        if (userStatus.getName().equals("właściciel"))
            walletUser.setAccepted_at(date);
        else
            walletUser.setAccepted_at(null);

        walletUser.setUserStatus(userStatus);
        wallet.addWalletUser(walletUser);
    }

    @Override
    public void save(WalletHolder walletHolder) {
        Wallet wallet = walletHolder.getWallet();
        List<String> userLoginList = walletHolder.getUserList();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        UserStatus ownerStatus = userStatusRepository.findByName("właściciel").orElseThrow(RuntimeException::new);
        UserStatus waitingStatus = userStatusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);

        // Save the wallet's owner
        saveUser(currentUserLogin, wallet, ownerStatus);

        // Save others wallet's members
        for (String memberLogin : userLoginList) {
            saveUser(memberLogin, wallet, waitingStatus);
        }

        walletRepository.save(wallet);
    }

    @Override
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> findById(int id) {
        return walletRepository.findById(id);
    }

    @Override
    public List<Wallet> findWallets(User user) {
        List<Wallet> walletList = new ArrayList<>();

        UserStatus ownerStatus = userStatusRepository.findByName("właściciel").orElseThrow(RuntimeException::new);
        UserStatus memberStatus = userStatusRepository.findByName("członek").orElseThrow(RuntimeException::new);

        Set<WalletUser> walletsOwner = walletUserRepository.findAllByUserStatusAndUser(ownerStatus, user);
        Set<WalletUser> walletsMember = walletUserRepository.findAllByUserStatusAndUser(memberStatus, user);

        walletsOwner.addAll(walletsMember);

        walletsOwner.forEach(walletUser -> walletList.add(walletUser.getWallet()));

        return walletList;
    }

    @Override
    public List<Map<String, Object>> findUserList(Wallet wallet) {
        List<Map<String, Object>> userList = new ArrayList<>();

        for (WalletUser walletUser : wallet.getWalletUserSet())
            if (walletUser.getUserStatus().getName().equals("właściciel")
                    || walletUser.getUserStatus().getName().equals("członek")) {
                Map<String, Object> userMap = new HashMap<>();

                userMap.put("userId", walletUser.getUser().getId());
                userMap.put("login", walletUser.getUser().getLogin());
                userMap.put("balance", walletUser.getBalance());

                userList.add(userMap);
            }

        return userList;
    }

    @Override
    public List<WalletUser> findWalletUserList(Wallet wallet) {
        List<WalletUser> walletUserList = new ArrayList<>();

        for (WalletUser walletUser : wallet.getWalletUserSet())
            if (walletUser.getUserStatus().getName().equals("właściciel")
                    || walletUser.getUserStatus().getName().equals("członek")) {
                walletUserList.add(walletUser);
            }

        return walletUserList;
    }

    @Override
    public List<Map<String, Object>> findAllUsers(Wallet wallet) {
        List<Map<String, Object>> userList = new ArrayList<>();

        for (WalletUser walletUser : wallet.getWalletUserSet())
            if (walletUser.getUserStatus().getName().equals("właściciel")
                    || walletUser.getUserStatus().getName().equals("członek")
                    || walletUser.getUserStatus().getName().equals("oczekujący")) {
                Map<String, Object> userMap = new HashMap<>();

                userMap.put("userId", walletUser.getUser().getId());
                userMap.put("login", walletUser.getUser().getLogin());

                userList.add(userMap);
            }

        return userList;
    }

    @Override
    public User findOwner(Wallet wallet) {
        for (WalletUser walletUser : wallet.getWalletUserSet())
            if (walletUser.getUserStatus().getName().equals("właściciel"))
                return walletUser.getUser();

        return null;
    }

    @Override
    public void simplifyDebts(Map<Integer, BigDecimal> balanceMap, List<DebtsHolder> debtsList) {
        BigDecimal minBalance = Collections.min(balanceMap.entrySet(), Map.Entry.comparingByValue()).getValue();
        BigDecimal maxBalance = Collections.max(balanceMap.entrySet(), Map.Entry.comparingByValue()).getValue();

        Integer minKey = Collections.min(balanceMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        Integer maxKey = Collections.max(balanceMap.entrySet(), Map.Entry.comparingByValue()).getKey();

        BigDecimal min = minBalance.min(maxBalance);
        if (minBalance.compareTo(BigDecimal.ZERO) < 0)
            min = minBalance.multiply(BigDecimal.valueOf(-1)).min(maxBalance);

        balanceMap.replace(minKey, balanceMap.get(minKey).add(min).setScale(2, RoundingMode.HALF_UP));
        balanceMap.replace(maxKey, balanceMap.get(maxKey).subtract(min).setScale(2, RoundingMode.HALF_UP));

        User debtor = userService.findById(minKey).orElseThrow(RuntimeException::new);
        User creditor = userService.findById(maxKey).orElseThrow(RuntimeException::new);

        DebtsHolder debtsHolder = new DebtsHolder(debtor, creditor, min);
        debtsList.add(debtsHolder);

        if (balanceMap.get(minKey).abs().compareTo(BigDecimal.valueOf(0.10)) > 0
                || balanceMap.get(maxKey).abs().compareTo(BigDecimal.valueOf(0.10)) > 0)
            simplifyDebts(balanceMap, debtsList);
    }
}
