package com.team.backend.service;

import com.team.backend.exception.StatusNotFoundException;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.exception.UserStatusNotFoundException;
import com.team.backend.exception.WalletUserNotFoundException;
import com.team.backend.helpers.DebtsHolder;
import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.*;
import com.team.backend.repository.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserStatusRepository userStatusRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final WalletUserRepository walletUserRepository;
    private final ListService listService;
    private final StatusRepository statusRepository;
    private final ListDetailService listDetailService;
    private final MessageService messageService;
    private final ExpenseService expenseService;
    private final CategoryRepository categoryRepository;

    public WalletServiceImpl(UserStatusRepository userStatusRepository, WalletRepository walletRepository,
                             @Lazy UserService userService, WalletUserRepository walletUserRepository,
                             ListService listService, StatusRepository statusRepository,
                             ListDetailService listDetailService, MessageService messageService,
                             @Lazy ExpenseService expenseService, CategoryRepository categoryRepository) {
        this.userStatusRepository = userStatusRepository;
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.walletUserRepository = walletUserRepository;
        this.listService = listService;
        this.statusRepository = statusRepository;
        this.listDetailService = listDetailService;
        this.messageService = messageService;
        this.expenseService = expenseService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void saveUser(String userLogin, Wallet wallet, UserStatus userStatus) {
        User user = userService.findByLogin(userLogin).orElseThrow(UserNotFoundException::new);

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

        UserStatus ownerStatus = userStatusRepository.findByName("właściciel")
                .orElseThrow(UserStatusNotFoundException::new);
        UserStatus waitingStatus = userStatusRepository.findByName("oczekujący")
                .orElseThrow(UserStatusNotFoundException::new);

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
    @Transactional
    public void delete(Wallet wallet) {
        expenseService.deleteAllByWallet(wallet);
        listService.deleteAllByWallet(wallet);
        messageService.deleteAllByWallet(wallet);

        walletRepository.delete(wallet);
    }

    @Override
    @Transactional
    public boolean delete(WalletUser walletUser, Wallet wallet, User user) {
        if (walletUser.getBalance().compareTo(BigDecimal.valueOf(0.00)) == 0
                && !user.equals(findOwner(wallet))) {
            leaveWallet(walletUser, wallet, user);

            return true;
        } else if (walletUser.getBalance().compareTo(BigDecimal.valueOf(0.00)) == 0
                && user.equals(findOwner(wallet)) && findWalletUserList(wallet).size() == 1) {
            delete(wallet);
            return true;
        } else if (walletUser.getBalance().compareTo(BigDecimal.valueOf(0.00)) == 0
                && user.equals(findOwner(wallet)) && findWalletUserList(wallet).size() > 1) {
            leaveWallet(walletUser, wallet, user);

            List<WalletUser> walletUserList = findWalletUserList(wallet);

            walletUserList.sort(Comparator.comparing(WalletUser::getAccepted_at));
            UserStatus ownerUserStatus = userStatusRepository.findByName("właściciel")
                    .orElseThrow(UserStatusNotFoundException::new);
            walletUserList.get(0).setUserStatus(ownerUserStatus);

            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteUser(Wallet wallet, User user) {
        WalletUser userDetail = walletUserRepository.findByWalletAndUser(wallet, user)
                .orElseThrow(WalletUserNotFoundException::new);

        return delete(userDetail, wallet, user);
    }

    @Override
    public void leaveWallet(WalletUser walletUser, Wallet wallet, User user) {
        UserStatus deletedUserStatus = userStatusRepository.findByName("usunięty")
                .orElseThrow(UserStatusNotFoundException::new);
        walletUser.setUserStatus(deletedUserStatus);
        walletUserRepository.save(walletUser);

        Status reservedStatus = statusRepository.findByName("zarezerwowany").orElseThrow(StatusNotFoundException::new);
        Status pendingStatus = statusRepository.findByName("oczekujący").orElseThrow(StatusNotFoundException::new);
        List<ShoppingList> shoppingList = listService.
                findAllByUserAndWalletAndStatus(user, wallet, reservedStatus);
        shoppingList.forEach(list -> {
            list.setStatus(pendingStatus);
            list.setUser(null);
            listService.save(list);
        });
        List<ShoppingList> walletShoppingList = listService.findAllByWallet(wallet);
        walletShoppingList.forEach(list -> listDetailService
                .findAllByUserAndListAndStatus(user, list, reservedStatus)
                .forEach(listDetail -> {
                    listDetail.setStatus(pendingStatus);
                    listDetail.setUser(null);
                    listDetailService.save(listDetail);
                }));
    }

    @Override
    public Optional<Wallet> findById(int id) {
        return walletRepository.findById(id);
    }

    @Override
    public List<Wallet> findWallets(User user) {
        List<Wallet> walletList = new ArrayList<>();

        UserStatus ownerStatus = userStatusRepository.findByName("właściciel")
                .orElseThrow(UserStatusNotFoundException::new);
        UserStatus memberStatus = userStatusRepository.findByName("członek")
                .orElseThrow(UserStatusNotFoundException::new);

        Set<WalletUser> walletsOwner = walletUserRepository.findAllByUserStatusAndUser(ownerStatus, user);
        Set<WalletUser> walletsMember = walletUserRepository.findAllByUserStatusAndUser(memberStatus, user);

        walletsOwner.addAll(walletsMember);

        walletsOwner.forEach(walletUser -> walletList.add(walletUser.getWallet()));

        return walletList;
    }

    @Override
    public List<Map<String, Object>> findUserList(Wallet wallet) {
        List<Map<String, Object>> userList = new ArrayList<>();
        User loggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        for (WalletUser walletUser : wallet.getWalletUserSet())
            if (walletUser.getUserStatus().getName().equals("właściciel")
                    || walletUser.getUserStatus().getName().equals("członek")) {
                Map<String, Object> userMap = new HashMap<>();

                User user = walletUser.getUser();
                userMap.put("userId", user.getId());
                userMap.put("login", user.getLogin());
                userMap.put("image", user.getImage());
                userMap.put("balance", walletUser.getBalance());
                userMap.put("debt", null);

                for (DebtsHolder debtsHolder : findDebts(wallet))
                    if ((debtsHolder.getDebtor().equals(user) || debtsHolder.getCreditor().equals(user))
                            && (debtsHolder.getDebtor().equals(loggedInUser) || debtsHolder.getCreditor().equals(loggedInUser)))
                        userMap.replace("debt", debtsHolder);

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
    public List<String> findDeletedUserList(Wallet wallet) {
        List<String> deletedUserList = new ArrayList<>();

        wallet.getWalletUserSet()
                .stream().filter(
                walletUser -> walletUser.getUserStatus().getName().equals("usunięty"))
                .forEach(
                        w -> deletedUserList.add(w.getUser().getLogin()));

        return deletedUserList;
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
                userMap.put("image", walletUser.getUser().getImage());

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

        if (minBalance.compareTo(BigDecimal.valueOf(0.00)) == 0 && maxBalance.compareTo(BigDecimal.valueOf(0.00)) == 0) {
            return;
        }

        Integer minKey = Collections.min(balanceMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        Integer maxKey = Collections.max(balanceMap.entrySet(), Map.Entry.comparingByValue()).getKey();

        BigDecimal min = minBalance.min(maxBalance);
        if (minBalance.compareTo(BigDecimal.ZERO) < 0)
            min = minBalance.multiply(BigDecimal.valueOf(-1)).min(maxBalance);

        balanceMap.replace(minKey, balanceMap.get(minKey).add(min).setScale(2, RoundingMode.HALF_UP));
        balanceMap.replace(maxKey, balanceMap.get(maxKey).subtract(min).setScale(2, RoundingMode.HALF_UP));

        User debtor = userService.findById(minKey).orElseThrow(UserNotFoundException::new);
        User creditor = userService.findById(maxKey).orElseThrow(UserNotFoundException::new);

        DebtsHolder debtsHolder = new DebtsHolder(debtor, creditor, min);
        debtsList.add(debtsHolder);

        if (balanceMap.get(minKey).abs().compareTo(BigDecimal.valueOf(0.10)) > 0
                || balanceMap.get(maxKey).abs().compareTo(BigDecimal.valueOf(0.10)) > 0)
            simplifyDebts(balanceMap, debtsList);
    }

    @Override
    public List<DebtsHolder> findDebts(Wallet wallet) {
        List<WalletUser> walletUserList = findWalletUserList(wallet);
        Map<Integer, BigDecimal> balanceMap = new HashMap<>();
        walletUserList.forEach(walletUser -> balanceMap.put(walletUser.getUser().getId(), walletUser.getBalance()));
        List<DebtsHolder> debtsList = new ArrayList<>();

        if (walletUserList.size() > 1)
            simplifyDebts(balanceMap, debtsList);

        return debtsList;
    }

    @Override
    public Map<String, Object> getOne(Wallet wallet) {
        User loggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        Map<String, Object> map = new HashMap<>();

        map.put("id", wallet.getId());
        map.put("name", wallet.getName());
        map.put("walletCategory", wallet.getWalletCategory());
        map.put("description", wallet.getDescription());
        map.put("owner", findOwner(wallet).getLogin());
        map.put("userListCounter", findUserList(wallet).size());
        map.put("walletExpensesCost", expenseService.calculateExpensesCost(wallet));
        map.put("userExpensesCost", expenseService.calculateExpensesCostForUser(wallet, loggedInUser));
        map.put("loggedInUserBalance", walletUserRepository.findByWalletAndUser(wallet, loggedInUser)
                .orElseThrow(WalletUserNotFoundException::new).getBalance());

        List<Map<String, Object>> userList = findUserList(wallet);
        map.put("userList", userList);

        return map;
    }

    @Override
    public List<Map<String, Object>> getAll() {
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        List<Wallet> wallets = findWallets(user);

        List<Map<String, Object>> walletsList = new ArrayList<>();

        for (Wallet wallet : wallets) {
            Map<String, Object> map = new HashMap<>();

            map.put("id", wallet.getId());
            map.put("name", wallet.getName());
            map.put("walletCategory", wallet.getWalletCategory());
            map.put("owner", findOwner(wallet).getLogin());
            map.put("userListCounter", findUserList(wallet).size());
            map.put("walletExpensesCost", expenseService.calculateExpensesCost(wallet));

            walletsList.add(map);
        }

        return walletsList;
    }

    @Override
    public Map<String, Object> returnStats(Wallet wallet, LocalDateTime from, LocalDateTime to) {
        List<Expense> expenseList = expenseService.findAllByWalletAndDateBetween(wallet, from, to);
        Map<String, Object> statsMap = new HashMap<>();

        if (expenseList.size() == 0)
            return statsMap;

        List<ExpenseDetail> expenseDetailList = new ArrayList<>();
        expenseList.forEach(expense -> expenseDetailList.addAll(expense.getExpenseDetailSet()));

        Map<User, BigDecimal> mapByUser = expenseDetailList.stream()
                .collect(Collectors.groupingBy(
                        ExpenseDetail::getUser,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                ExpenseDetail::getCost,
                                BigDecimal::add)));

        BigDecimal maxExpensesValue = Collections.max(mapByUser.values());

        List<String> maxUsersList = mapByUser.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxExpensesValue))
                .map(entry -> entry.getKey().getLogin())
                .collect(Collectors.toList());

        statsMap.put("maxUsersList", maxUsersList);
        statsMap.put("maxExpensesValue", maxExpensesValue);

        BigDecimal expensesCost = BigDecimal.valueOf(0.0);
        for (Expense expense : expenseList) {
            expensesCost = expensesCost.add(expense.getTotal_cost());
        }

        statsMap.put("totalCost", expensesCost);

        Map<Category, List<Expense>> mapByCategory =
                expenseList.stream().collect(Collectors.groupingBy(Expense::getCategory));

        for (Category category : categoryRepository.findAll()) {
            List<Expense> expenses = mapByCategory.getOrDefault(category, Collections.emptyList());
            expensesCost = BigDecimal.valueOf(0.0);
            for (Expense expense : expenses) {
                expensesCost = expensesCost.add(expense.getTotal_cost());
            }
            statsMap.put(category.getName(), expensesCost);
        }

        return statsMap;
    }

    @Override
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }
}
