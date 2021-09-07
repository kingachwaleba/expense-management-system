package com.team.backend.service;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.*;
import com.team.backend.repository.ExpenseRepository;
import com.team.backend.repository.PaymentStatusRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final PaymentStatusRepository paymentStatusRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService, PaymentStatusRepository paymentStatusRepository) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Override
    public void save(ExpenseHolder expenseHolder, Wallet wallet) {
        Expense expense = expenseHolder.getExpense();
        List<String> userList = expenseHolder.getUserList();
        User owner = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        LocalDateTime date = LocalDateTime.now();

        expense.setWallet(wallet);
        expense.setUser(owner);
        expense.setDate(date);

        PaymentStatus waitingStatus = paymentStatusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);
        PaymentStatus completedStatus = paymentStatusRepository.findByName("zrealizowany").orElseThrow(RuntimeException::new);
        
        BigDecimal cost = expense.getTotal_cost().divide(BigDecimal.valueOf(userList.size()), 2, RoundingMode.CEILING);

        for (String login : userList) {
            User member = userService.findByLogin(login).orElseThrow(RuntimeException::new);

            ExpenseDetail expenseDetail = new ExpenseDetail();

            expenseDetail.setCost(cost);
            expenseDetail.setUser(member);

            if (member.getLogin().equals(owner.getLogin()))
                expenseDetail.setPaymentStatus(completedStatus);
            else
                expenseDetail.setPaymentStatus(waitingStatus);

            expense.addExpenseDetail(expenseDetail);
        }

        expenseRepository.save(expense);
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
}
