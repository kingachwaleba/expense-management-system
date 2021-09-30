package com.team.backend.service;

import com.team.backend.exception.UserNotFoundException;
import com.team.backend.exception.WalletNotFoundException;
import com.team.backend.model.*;
import com.team.backend.repository.UserRepository;
import com.team.backend.repository.WalletUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final WalletUserRepository walletUserRepository;
    private final WalletService walletService;
    private final MessageService messageService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                           WalletUserRepository walletUserRepository, WalletService walletService,
                           MessageService messageService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.walletUserRepository = walletUserRepository;
        this.walletService = walletService;
        this.messageService = messageService;
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setDeleted(String.valueOf(User.AccountType.N));
        user.setImage(null);
        userRepository.save(user);
    }

    @Override
    public void saveDeleted(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean ifAccountDeleted(User user) {
        List<WalletUser> walletUserList = walletUserRepository.findAllByUser(user);
        for (WalletUser walletUser : walletUserList) {
            Wallet wallet = walletUser.getWallet();
            if (!walletService.delete(walletUser, wallet, user))
                return false;
        }

        List<Message> notificationList = messageService.findAllByReceiver(user);
        for (Message message : notificationList) {
            messageService.delete(message);
        }

        user.setDeleted(String.valueOf(User.AccountType.Y));
        saveDeleted(user);

        return true;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findByDeletedAndLoginContaining(String deleted, String infix) {
        return userRepository.findByDeletedAndLoginContaining(deleted, infix);
    }

    @Override
    public Boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean checkIfValidOldPassword(User user, String oldPassword) {
        return bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    public void changeUserImage(User user, String imageUrl) {
        user.setImage(imageUrl);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        return userRepository.findByLogin(currentUserLogin);
    }

    @Override
    public BigDecimal calculateUserBalance(User user) {
        List<WalletUser> balanceList = walletUserRepository.findAllByUser(user);
        BigDecimal totalBalance = BigDecimal.valueOf(0.00);

        for (WalletUser walletUser : balanceList)
            totalBalance = totalBalance.add(walletUser.getBalance());

        return totalBalance;
    }

    @Override
    public List<String> findUserForWallet(int id, String infix) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);
        List<Map<String, Object>> userList = walletService.findAllUsers(wallet);

        List<User> userListInfix = findByDeletedAndLoginContaining(String.valueOf(User.AccountType.N), infix);
        List<String> userLoginList = new ArrayList<>();
        for (User user : userListInfix) {
            Map<String, Object> userMap = new HashMap<>();

            userMap.put("userId", user.getId());
            userMap.put("login", user.getLogin());

            if (!userList.contains(userMap))
                userLoginList.add(user.getLogin());
        }

        return userLoginList;
    }

    @Override
    public List<String> findUser(String infix) {
        User loggedInUser = findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        List<User> userList = findByDeletedAndLoginContaining(String.valueOf(User.AccountType.N), infix);
        List<String> userLoginList = new ArrayList<>();
        for (User user : userList) {
            if (user.getId() != loggedInUser.getId())
                userLoginList.add(user.getLogin());
        }

        return userLoginList;
    }

    @Override
    public Map<String, String> findUserDetails() {
        User user = findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        List<Wallet> wallets = walletService.findWallets(user);

        Map<String, String> userDetailsMap = new HashMap<>();
        userDetailsMap.put("id", String.valueOf(user.getId()));
        userDetailsMap.put("login", user.getLogin());
        userDetailsMap.put("email", user.getEmail());
        userDetailsMap.put("image", user.getImage());
        userDetailsMap.put("walletsNumber", String.valueOf(wallets.size()));
        userDetailsMap.put("userBalance", String.valueOf(calculateUserBalance(user)));

        return userDetailsMap;
    }
}
