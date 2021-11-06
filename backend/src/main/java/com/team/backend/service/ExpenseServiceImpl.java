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
    public void editUserList(Expense updatedExpense, Expense newExpense, List<String> userList) {
        Wallet wallet = updatedExpense.getWallet();
        List<String> tempList = new ArrayList<>();
        updatedExpense.getExpenseDetailSet().forEach(expenseDetail -> tempList.add(expenseDetail.getUser().getLogin()));

        if (!userList.equals(tempList)) {
            BigDecimal cost = updatedExpense.getTotal_cost().divide(
                    new BigDecimal(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.HALF_UP);
            BigDecimal totalCost;

            calculateNewBalance(wallet, updatedExpense, cost);

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
                cost = updatedExpense.getTotal_cost().divide(
                        new BigDecimal(newSize), 2, RoundingMode.HALF_UP);

                totalCost = updatedExpense.getTotal_cost();

                for (String login : userList) {
                    User member = userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

                    Optional<ExpenseDetail> optionalExpenseDetail = expenseDetailRepository
                            .findByUserAndExpense(member, updatedExpense);

                    if (optionalExpenseDetail.isEmpty()) {
                        ExpenseDetail expenseDetail = new ExpenseDetail();

                        expenseDetail.setCost(cost);
                        expenseDetail.setUser(member);
                        expenseDetail.setExpense(updatedExpense);

                        totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
                        if (userList.size() >= 2 && userList.get(userList.size() - 2).equals(login)) {
                            expenseDetail.setCost(totalCost);
                        }

                        updatedExpense.addExpenseDetail(expenseDetail);
                        expenseDetailRepository.save(expenseDetail);
                    }
                    else {
                        ExpenseDetail expenseDetail = optionalExpenseDetail.get();

                        expenseDetail.setCost(cost);
                        expenseDetail.setUser(member);
                        expenseDetail.setExpense(updatedExpense);

                        totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
                        if (userList.size() >= 2 && userList.get(userList.size() - 2).equals(login)) {
                            expenseDetail.setCost(totalCost);
                        }

                        expenseDetailRepository.save(expenseDetail);
                    }

                }
            }

            save(updatedExpense);

            temp(wallet, updatedExpense);
        }
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
            BigDecimal cost = oldCost.subtract(newCost).divide(
                    new BigDecimal(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.HALF_UP);

            calculateNewBalance(wallet, updatedExpense, cost);

            ArrayList<ExpenseDetail> expenseDetails = new ArrayList<>(updatedExpense.getExpenseDetailSet());
            for (ExpenseDetail expenseDetail : expenseDetails) {
                cost = updatedExpense.getTotal_cost().divide(new BigDecimal(updatedExpense
                        .getExpenseDetailSet().size()), 2, RoundingMode.HALF_UP);

                expenseDetail.setCost(cost);

                totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
                if (expenseDetails.size() >= 2 && expenseDetails.get(expenseDetails.size() - 2).equals(expenseDetail)) {
                    expenseDetail.setCost(totalCost);
                }
            }

            temp(wallet, updatedExpense);
        }
    }

    @Override
    public void deleteExpense(Expense expense) {
        Wallet wallet = expense.getWallet();
        BigDecimal cost = expense.getTotal_cost().divide(
                BigDecimal.valueOf(expense.getExpenseDetailSet().size()), 2, RoundingMode.HALF_UP);

        calculateNewBalance(wallet, expense, cost);
        delete(expense);
    }

    @Override
    public void calculateNewBalance(Wallet wallet, Expense expense, BigDecimal cost) {
        User owner = expense.getUser();
        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        List<ExpenseDetail> expenseDetailList = new ArrayList<>(expense.getExpenseDetailSet());
        WalletUser ownerDetails = walletUserList.stream()
                .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(WalletUserNotFoundException::new);

        List<WalletUser> tempList = new ArrayList<>();
        for (WalletUser w : walletUserList)
            for (ExpenseDetail expenseDetail : expenseDetailList)
                if (expenseDetail.getUser().equals(w.getUser()))
                    tempList.add(w);

        tempList.stream().filter(walletUser -> !walletUser.equals(ownerDetails))
                .forEach(wu -> {
                    BigDecimal oldBalance = wu.getBalance();

                    BigDecimal costExpenseDetail = expenseDetailRepository
                            .findByUserAndExpense(wu.getUser(), expense)
                            .orElseThrow(ExpenseDetailNotFoundException::new).getCost();

                    wu.setBalance(oldBalance.add(costExpenseDetail).setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(wu);

                    oldBalance = ownerDetails.getBalance();
                    ownerDetails
                            .setBalance(oldBalance.subtract(costExpenseDetail).setScale(2, RoundingMode.HALF_UP));
                    walletUserRepository.save(ownerDetails);
                });
    }

    public void temp(Wallet wallet, Expense expense) {
        User owner = expense.getUser();
        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        List<ExpenseDetail> expenseDetailList = new ArrayList<>(expense.getExpenseDetailSet());
        WalletUser ownerDetails = walletUserList.stream()
                .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(WalletUserNotFoundException::new);

        List<WalletUser> tempList = new ArrayList<>();
        for (WalletUser w : walletUserList)
            for (ExpenseDetail expenseDetail : expenseDetailList)
                if (expenseDetail.getUser().equals(w.getUser()))
                    tempList.add(w);

        BigDecimal totalCost = expense.getTotal_cost();

        List<WalletUser> tempList2 = new ArrayList<>();
        for (WalletUser walletUser : tempList) {
                tempList2.add(walletUser);
        }

        for (WalletUser wu : tempList2) {
            BigDecimal oldBalance = wu.getBalance();
            BigDecimal cost = expenseDetailRepository
                    .findByUserAndExpense(wu.getUser(), expense)
                    .orElseThrow(ExpenseDetailNotFoundException::new).getCost();

            if (!wu.equals(ownerDetails)) {
                wu.setBalance(oldBalance.subtract(cost).setScale(2, RoundingMode.HALF_UP));
                walletUserRepository.save(wu);

                oldBalance = ownerDetails.getBalance();
                ownerDetails.setBalance(oldBalance.add(cost).setScale(2, RoundingMode.HALF_UP));
                walletUserRepository.save(ownerDetails);
            }

            totalCost = totalCost.subtract(cost).setScale(2, RoundingMode.HALF_UP);
            if (tempList2.size() >= 2 && tempList2.get(tempList2.size() - 2).equals(wu)) {
                cost = totalCost;
            }
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
        List<Expense> expenseList = findAllByWalletAndUser(wallet, user);
        BigDecimal totalCost = BigDecimal.valueOf(0.00);

        for (Expense expense : expenseList)
            totalCost = totalCost.add(expense.getTotal_cost());

        return totalCost;
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
