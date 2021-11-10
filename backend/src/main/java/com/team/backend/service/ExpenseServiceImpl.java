package com.team.backend.service;

import com.team.backend.exception.*;
import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.*;
import com.team.backend.repository.ExpenseDetailRepository;
import com.team.backend.repository.ExpenseRepository;
import com.team.backend.repository.WalletUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final WalletUserRepository walletUserRepository;
    private final ExpenseDetailRepository expenseDetailRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService, WalletService walletService,
                              WalletUserRepository walletUserRepository,
                              ExpenseDetailRepository expenseDetailRepository) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.walletService = walletService;
        this.walletUserRepository = walletUserRepository;
        this.expenseDetailRepository = expenseDetailRepository;
    }

    @Override
    public void save(ExpenseHolder expenseHolder, int walletId) {
        Wallet wallet = walletService.findById(walletId).orElseThrow(WalletNotFoundException::new);
        Expense expense = expenseHolder.getExpense();
        List<String> userList = expenseHolder.getUserList();
        User owner = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        Map<Integer, BigDecimal> balanceMap = new HashMap<>();

        LocalDateTime date = LocalDateTime.now();

        expense.setWallet(wallet);
        expense.setUser(owner);
        expense.setDate(date);

        BigDecimal cost = expense.getTotal_cost().divide(new BigDecimal(userList.size()), 2, RoundingMode.HALF_UP);
        BigDecimal totalCost = expense.getTotal_cost();

        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);

        WalletUser walletUser;
        BigDecimal balance;

        WalletUser walletOwner = walletUserList.stream()
                .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(WalletUserNotFoundException::new);
        BigDecimal ownerBalance = walletOwner.getBalance();

        for (String login : userList) {
            User member = userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

            ExpenseDetail expenseDetail = new ExpenseDetail();
            expenseDetail.setCost(cost);
            expenseDetail.setUser(member);

            walletUser = walletUserList.stream()
                    .filter(temp -> temp.getUser().equals(member)).findAny()
                    .orElseThrow(WalletUserNotFoundException::new);

            if (!login.equals(walletOwner.getUser().getLogin())) {
                balance = walletUser.getBalance().subtract(cost).setScale(2, RoundingMode.HALF_UP);
                ownerBalance = ownerBalance.add(cost).setScale(2, RoundingMode.HALF_UP);
                balanceMap.put(walletUser.getId(), balance);
            }

            expense.addExpenseDetail(expenseDetail);

            totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
            if (userList.size() >= 2 && userList.get(userList.size() - 2).equals(login)) {
                cost = totalCost;
            }
        }

        balanceMap.put(walletOwner.getId(), ownerBalance);

        expenseRepository.save(expense);

        balanceMap.forEach(
                (id, newBalance) ->
                        walletUserRepository.findById(id).orElseThrow(WalletUserNotFoundException::new)
                                .setBalance(newBalance)
        );
    }

    @Override
    public void save(Expense expense) {
        expenseRepository.save(expense);
    }

    @Override
    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

    @Override
    public void deleteAllByWallet(Wallet wallet) {
        expenseRepository.deleteAllByWallet(wallet);
    }

    @Override
    public Optional<Expense> findById(int id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> findAllByWalletOrderByDate(Wallet wallet) {
        return expenseRepository.findAllByWalletOrderByDate(wallet);
    }

    @Override
    public List<Expense> findAllByWalletAndUser(Wallet wallet, User user) {
        return expenseRepository.findAllByWalletAndUser(wallet, user);
    }

    @Override
    public List<Expense> findAllByWalletAndDateBetween(Wallet wallet, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return expenseRepository.findAllByWalletAndDateBetween(wallet, dateFrom, dateTo);
    }

    @Override
    public List<WalletUser> findWalletUsersInExpense(List<ExpenseDetail> expenseDetailList,
                                                     List<WalletUser> walletUserList) {
        List<WalletUser> walletUsersInExpenseList = new ArrayList<>();

        for (WalletUser walletUser : walletUserList)
            for (ExpenseDetail expenseDetail : expenseDetailList)
                if (expenseDetail.getUser().equals(walletUser.getUser()))
                    walletUsersInExpenseList.add(walletUser);

        return walletUsersInExpenseList;
    }

    @Override
    public void editUserList(Expense updatedExpense, Expense newExpense, List<String> userList) {
        Wallet wallet = updatedExpense.getWallet();
        List<String> tempList = new ArrayList<>();
        updatedExpense.getExpenseDetailSet().forEach(expenseDetail -> tempList.add(expenseDetail.getUser().getLogin()));

        if (!userList.equals(tempList)) {
            cleanBalance(wallet, updatedExpense);

            for (String login : tempList) {
                if (!userList.contains(login)) {
                    User temp = userService.findByLogin(login).orElseThrow(UserNotFoundException::new);
                    ExpenseDetail expenseDetail = expenseDetailRepository
                            .findByUserAndExpense(temp, updatedExpense)
                            .orElseThrow(ExpenseDetailNotFoundException::new);
                    updatedExpense.getExpenseDetailSet().remove(expenseDetail);
                    expenseDetailRepository.delete(expenseDetail);
                    userList.remove(login);
                }
            }

            if (userList.size() != 0) {
                int newSize = userList.size();
                BigDecimal cost = updatedExpense.getTotal_cost().divide(
                        new BigDecimal(newSize), 2, RoundingMode.HALF_UP);
                BigDecimal totalCost = updatedExpense.getTotal_cost();

                for (String login : userList) {
                    User member = userService.findByLogin(login).orElseThrow(UserNotFoundException::new);
                    Optional<ExpenseDetail> optionalExpenseDetail = expenseDetailRepository
                            .findByUserAndExpense(member, updatedExpense);

                    ExpenseDetail expenseDetail;
                    if (optionalExpenseDetail.isEmpty()) {
                        expenseDetail = new ExpenseDetail();
                        totalCost =
                                getBigDecimal(updatedExpense, userList, cost, totalCost, login, member, expenseDetail);
                        updatedExpense.addExpenseDetail(expenseDetail);
                    } else {
                        expenseDetail = optionalExpenseDetail.get();
                        totalCost =
                                getBigDecimal(updatedExpense, userList, cost, totalCost, login, member, expenseDetail);
                    }
                    expenseDetailRepository.save(expenseDetail);
                }
            }

            save(updatedExpense);
            calculateNewBalance(wallet, updatedExpense, false);
        }
    }

    private BigDecimal getBigDecimal(Expense updatedExpense, List<String> userList, BigDecimal cost,
                                     BigDecimal totalCost, String login, User member, ExpenseDetail expenseDetail) {
        expenseDetail.setCost(cost);
        expenseDetail.setUser(member);
        expenseDetail.setExpense(updatedExpense);

        totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
        if (userList.size() >= 2 && userList.get(userList.size() - 2).equals(login)) {
            expenseDetail.setCost(totalCost);
        }
        return totalCost;
    }

    @Override
    public void edit(Expense updatedExpense, Expense newExpense) {
        BigDecimal oldCost = updatedExpense.getTotal_cost();
        BigDecimal newCost = newExpense.getTotal_cost();
        updatedExpense.setName(newExpense.getName());
        updatedExpense.setReceipt_image(newExpense.getReceipt_image());
        updatedExpense.setTotal_cost(newExpense.getTotal_cost());
        BigDecimal totalCost = newExpense.getTotal_cost();

        updatedExpense.setCategory(newExpense.getCategory());

        save(updatedExpense);

        if (oldCost.compareTo(newCost) != 0) {
            Wallet wallet = updatedExpense.getWallet();
            cleanBalance(wallet, updatedExpense);

            ArrayList<ExpenseDetail> expenseDetails = new ArrayList<>(updatedExpense.getExpenseDetailSet());
            for (ExpenseDetail expenseDetail : expenseDetails) {
                BigDecimal cost = updatedExpense.getTotal_cost().divide(new BigDecimal(updatedExpense
                        .getExpenseDetailSet().size()), 2, RoundingMode.HALF_UP);

                expenseDetail.setCost(cost);

                totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
                if (expenseDetails.size() >= 2 && expenseDetails.get(expenseDetails.size() - 2).equals(expenseDetail)) {
                    expenseDetail.setCost(totalCost);
                }
            }

            calculateNewBalance(wallet, updatedExpense, false);
        }
    }

    @Override
    public void deleteExpense(Expense expense) {
        Wallet wallet = expense.getWallet();

        calculateNewBalance(wallet, expense, true);
        delete(expense);
    }

    @Override
    public void cleanBalance(Wallet wallet, Expense expense) {
        User owner = expense.getUser();
        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        List<ExpenseDetail> expenseDetailList = new ArrayList<>(expense.getExpenseDetailSet());
        WalletUser ownerDetails = walletUserList.stream()
                .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(WalletUserNotFoundException::new);
        List<WalletUser> walletUsersInExpenseList = findWalletUsersInExpense(expenseDetailList, walletUserList);

        walletUsersInExpenseList.stream().filter(walletUser -> !walletUser.equals(ownerDetails))
                .forEach(walletUser -> {
                    BigDecimal oldBalance = walletUser.getBalance();
                    BigDecimal costExpenseDetail = expenseDetailRepository
                            .findByUserAndExpense(walletUser.getUser(), expense)
                            .orElseThrow(ExpenseDetailNotFoundException::new).getCost();

                    walletUser.setBalance(oldBalance.add(costExpenseDetail).setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(walletUser);

                    oldBalance = ownerDetails.getBalance();
                    ownerDetails
                            .setBalance(oldBalance.subtract(costExpenseDetail)
                                    .setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(ownerDetails);
                });
    }

    public void calculateNewBalance(Wallet wallet, Expense expense, boolean ifDeleted) {
        User owner = expense.getUser();
        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        List<ExpenseDetail> expenseDetailList = new ArrayList<>(expense.getExpenseDetailSet());
        WalletUser ownerDetails = walletUserList.stream()
                .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(WalletUserNotFoundException::new);
        List<WalletUser> walletUsersInExpenseList = findWalletUsersInExpense(expenseDetailList, walletUserList);
        BigDecimal totalCost = expense.getTotal_cost();

        for (WalletUser walletUser : walletUsersInExpenseList) {
            BigDecimal oldBalance = walletUser.getBalance();
            BigDecimal cost = expenseDetailRepository
                    .findByUserAndExpense(walletUser.getUser(), expense)
                    .orElseThrow(ExpenseDetailNotFoundException::new).getCost();

            if (!ifDeleted) {
                if (!walletUser.equals(ownerDetails)) {
                    walletUser.setBalance(oldBalance.subtract(cost).setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(walletUser);

                    oldBalance = ownerDetails.getBalance();
                    ownerDetails.setBalance(oldBalance.add(cost).setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(ownerDetails);
                }
            }
            else {
                if (!walletUser.equals(ownerDetails)) {
                    walletUser.setBalance(oldBalance.add(cost).setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(walletUser);

                    oldBalance = ownerDetails.getBalance();
                    ownerDetails.setBalance(oldBalance.subtract(cost).setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(ownerDetails);
                }
            }

            totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
            if (walletUsersInExpenseList.size() >= 2
                    && walletUsersInExpenseList.get(walletUsersInExpenseList.size() - 2).equals(walletUser))
                cost = totalCost;
        }
    }

    @Override
    public BigDecimal calculateExpensesCost(Wallet wallet) {
        List<Expense> expenseList = findAllByWalletOrderByDate(wallet);
        BigDecimal totalCost = BigDecimal.valueOf(0.00);

        for (Expense expense : expenseList)
            totalCost = totalCost.add(expense.getTotal_cost());

        return totalCost;
    }

    @Override
    public BigDecimal calculateExpensesCostForUser(Wallet wallet, User user) {
        List<Expense> expenseList = findAllByWalletOrderByDate(wallet);
        List<ExpenseDetail> expenseDetailList = new ArrayList<>();
        expenseList.forEach(expense -> expenseDetailList.addAll(expense.getExpenseDetailSet()));
        Map<User, BigDecimal> mapByUser = calculateUserExpenses(expenseDetailList);

        if (mapByUser.get(user) == null)
            return BigDecimal.valueOf(0.00);

        return mapByUser.get(user);
    }

    @Override
    public Map<User, BigDecimal> calculateUserExpenses(List<ExpenseDetail> expenseDetailList) {

        return expenseDetailList.stream()
                .collect(Collectors.groupingBy(
                        ExpenseDetail::getUser,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                ExpenseDetail::getCost,
                                BigDecimal::add)));
    }

    @Override
    public Map<String, Object> getOne(int id) {
        Expense expense = findById(id).orElseThrow(ExpenseNotFoundException::new);
        List<String> deletedUserList = walletService.findDeletedUserList(expense.getWallet());
        Map<String, Object> map = new HashMap<>();
        map.put("expense", expense);
        map.put("deletedUserList", deletedUserList);

        return map;
    }

    @Override
    public List<Expense> getAll(int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        return findAllByWalletOrderByDate(wallet);
    }

    @Override
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }
}
