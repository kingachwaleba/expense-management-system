package com.team.backend.service;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.*;
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

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService, WalletService walletService,
                              WalletUserRepository walletUserRepository) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.walletService = walletService;
        this.walletUserRepository = walletUserRepository;
    }

    @Override
    public void save(ExpenseHolder expenseHolder, Wallet wallet) {
        Expense expense = expenseHolder.getExpense();
        List<String> userList = expenseHolder.getUserList();
        User owner = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);
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
            User member = userService.findByLogin(login).orElseThrow(RuntimeException::new);

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
    public Optional<Expense> findById(int id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> findAllByWalletOrderByDate(Wallet wallet) {
        return expenseRepository.findAllByWalletOrderByDate(wallet);
    }

    @Override
    public void edit(Expense updatedExpense, Expense newExpense) {
        BigDecimal oldCost = updatedExpense.getTotal_cost();
        BigDecimal newCost = newExpense.getTotal_cost();
        updatedExpense.setName(newExpense.getName());
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
}
