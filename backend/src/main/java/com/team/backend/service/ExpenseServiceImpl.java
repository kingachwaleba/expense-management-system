package com.team.backend.service;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.*;
import com.team.backend.repository.ExpenseRepository;
import com.team.backend.repository.PaymentStatusRepository;
import com.team.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, PaymentStatusRepository paymentStatusRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Override
    public void save(ExpenseHolder expenseHolder, Wallet wallet) {
        Expense expense = expenseHolder.getExpense();
        List<String> userList = expenseHolder.getUserList();

        // Get current logged in user and set it
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        User owner = userRepository.findByLogin(currentUserLogin).orElseThrow(RuntimeException::new);

        LocalDateTime date = LocalDateTime.now();

        expense.setWallet(wallet);
        expense.setUser(owner);
        expense.setDate(date);

        PaymentStatus waitingStatus = paymentStatusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);
        PaymentStatus completedStatus = paymentStatusRepository.findByName("zrealizowany").orElseThrow(RuntimeException::new);

        BigDecimal cost = expense.getTotal_cost().divide(BigDecimal.valueOf(expense
                .getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

        ExpenseDetail expenseDetail = new ExpenseDetail();
        expenseDetail.setCost(cost);
        expenseDetail.setUser(owner);
        expenseDetail.setPaymentStatus(completedStatus);

        expense.addExpenseDetail(expenseDetail);

        for (String login : userList) {
            User member = userRepository.findByLogin(login).orElseThrow(RuntimeException::new);

            expenseDetail = new ExpenseDetail();

            expenseDetail.setCost(cost);
            expenseDetail.setUser(member);
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
    public Optional<Expense> findById(int id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> findAllByWalletOrderByDate(Wallet wallet) {
        return expenseRepository.findAllByWalletOrderByDate(wallet);
    }
}
