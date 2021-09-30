package com.team.backend.service;

import com.team.backend.exception.UserNotFoundException;
import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.*;
import com.team.backend.repository.ExpenseDetailRepository;
import com.team.backend.repository.ExpenseRepository;
import com.team.backend.repository.WalletUserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

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
        Wallet wallet = walletService.findById(walletId).orElseThrow(RuntimeException::new);
        Expense expense = expenseHolder.getExpense();
        List<String> userList = expenseHolder.getUserList();
        User owner = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        Map<Integer, BigDecimal> balanceMap = new HashMap<>();

        LocalDateTime date = LocalDateTime.now();

        expense.setWallet(wallet);
        expense.setUser(owner);
        expense.setDate(date);

        BigDecimal cost = expense.getTotal_cost().divide(BigDecimal.valueOf(userList.size()), 2, RoundingMode.CEILING);

        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);

        WalletUser walletUser = walletUserList.stream()
                .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(RuntimeException::new);
        BigDecimal balance = walletUser.getBalance().add(expense.getTotal_cost());
        walletUser.setBalance(balance);

        for (String login : userList) {
            User member = userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

            ExpenseDetail expenseDetail = new ExpenseDetail();

            expenseDetail.setCost(cost);
            expenseDetail.setUser(member);

            walletUser = walletUserList.stream()
                    .filter(temp -> temp.getUser().equals(member)).findAny().orElseThrow(RuntimeException::new);
            balance = walletUser.getBalance().subtract(cost);
            balanceMap.put(walletUser.getId(), balance);

            expense.addExpenseDetail(expenseDetail);
        }

        expenseRepository.save(expense);

        balanceMap.forEach(
                (id, newBalance) ->
                        walletUserRepository.findById(id).orElseThrow(RuntimeException::new).setBalance(newBalance));
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
    public void editUserList(Expense updatedExpense, Expense newExpense, List<String> userList) {
        Wallet wallet = updatedExpense.getWallet();
        List<String> tempList = new ArrayList<>();
        updatedExpense.getExpenseDetailSet().forEach(expenseDetail -> tempList.add(expenseDetail.getUser().getLogin()));

        if (!userList.equals(tempList)) {
            BigDecimal cost = updatedExpense.getTotal_cost().divide(
                    BigDecimal.valueOf(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

            calculateNewBalance(wallet, updatedExpense, cost);

            for (String login : tempList) {
                if (!userList.contains(login)) {
                    User temp = userService.findByLogin(login).orElseThrow(RuntimeException::new);
                    ExpenseDetail expenseDetail = expenseDetailRepository
                            .findByUserAndExpense(temp, updatedExpense).orElseThrow(RuntimeException::new);
                    updatedExpense.getExpenseDetailSet().remove(expenseDetail);
                    expenseDetailRepository.delete(expenseDetail);
                }
                userList.remove(login);
            }
            if (userList.size() != 0) {
                int newSize = updatedExpense.getExpenseDetailSet().size() + userList.size();
                cost = updatedExpense.getTotal_cost().divide(
                        BigDecimal.valueOf(newSize), 2, RoundingMode.CEILING);

                BigDecimal finalCost = cost;
                userList.forEach(login -> {
                    User member = userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

                    ExpenseDetail expenseDetail = new ExpenseDetail();

                    expenseDetail.setCost(finalCost);
                    expenseDetail.setUser(member);
                    expenseDetail.setExpense(updatedExpense);

                    updatedExpense.addExpenseDetail(expenseDetail);
                    expenseDetailRepository.save(expenseDetail);
                });

                updatedExpense.getExpenseDetailSet().forEach(expenseDetail -> {
                    expenseDetail.setCost(finalCost);
                    expenseDetailRepository.save(expenseDetail);
                });
            }

            cost = updatedExpense.getTotal_cost().divide(
                    BigDecimal.valueOf(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.CEILING)
                    .multiply(BigDecimal.valueOf(-1));

            calculateNewBalance(wallet, updatedExpense, cost);
        }
    }

    @Override
    public void edit(Expense updatedExpense, Expense newExpense) {
        BigDecimal oldCost = updatedExpense.getTotal_cost();
        BigDecimal newCost = newExpense.getTotal_cost();
        updatedExpense.setName(newExpense.getName());
        updatedExpense.setReceipt_image(newExpense.getReceipt_image());
        updatedExpense.setTotal_cost(newExpense.getTotal_cost());

        for (ExpenseDetail expenseDetail : updatedExpense.getExpenseDetailSet()) {
            BigDecimal cost = updatedExpense.getTotal_cost().divide(BigDecimal.valueOf(updatedExpense
                    .getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

            expenseDetail.setCost(cost);
        }

        updatedExpense.setCategory(newExpense.getCategory());
        updatedExpense.setPeriod(newExpense.getPeriod());

        save(updatedExpense);

        if (oldCost.compareTo(newCost) != 0) {
            Wallet wallet = updatedExpense.getWallet();
            BigDecimal cost = oldCost.subtract(newCost).divide(
                    BigDecimal.valueOf(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

            calculateNewBalance(wallet, updatedExpense, cost);
        }
    }

    @Override
    public void deleteExpense(Expense expense) {
        Wallet wallet = expense.getWallet();
        BigDecimal cost = expense.getTotal_cost().divide(
                BigDecimal.valueOf(expense.getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

        delete(expense);
        calculateNewBalance(wallet, expense, cost);
    }

    @Override
    public void calculateNewBalance(Wallet wallet, Expense expense, BigDecimal cost) {
        User owner = expense.getUser();
        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        List<ExpenseDetail> expenseDetailList = new ArrayList<>(expense.getExpenseDetailSet());
        WalletUser ownerDetails = walletUserList.stream()
                .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(RuntimeException::new);

        List<WalletUser> tempList = new ArrayList<>();
        for (WalletUser w : walletUserList)
            for (ExpenseDetail expenseDetail : expenseDetailList)
                if (expenseDetail.getUser().equals(w.getUser()))
                    tempList.add(w);

        tempList.stream().filter(walletUser -> !walletUser.equals(ownerDetails))
                .forEach(wu -> {
                    BigDecimal oldBalance = wu.getBalance();
                    wu.setBalance(oldBalance.add(cost));
                    walletUserRepository.save(wu);

                    oldBalance = ownerDetails.getBalance();
                    ownerDetails.setBalance(oldBalance.subtract(cost));
                    walletUserRepository.save(ownerDetails);
                });
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
        List<Expense> expenseList = findAllByWalletAndUser(wallet, user);
        BigDecimal totalCost = BigDecimal.valueOf(0.00);

        for (Expense expense : expenseList)
            totalCost = totalCost.add(expense.getTotal_cost());

        return totalCost;
    }

    @Override
    public Map<String, Object> getOne(int id) {
        Expense expense = findById(id).orElseThrow(RuntimeException::new);
        List<String> deletedUserList = walletService.findDeletedUserList(expense.getWallet());
        Map<String, Object> map = new HashMap<>();
        map.put("expense", expense);
        map.put("deletedUserList", deletedUserList);

        return map;
    }

    @Override
    public Map<String, Object> getAll(int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        List<String> deletedUserList = walletService.findDeletedUserList(wallet);
        Map<String, Object> map = new HashMap<>();
        map.put("allExpenses", findAllByWalletOrderByDate(wallet));
        map.put("deletedUserList", deletedUserList);

        return map;
    }
}
