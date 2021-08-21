package com.team.backend.service;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Expense;
import com.team.backend.model.Wallet;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    void save(ExpenseHolder expenseHolder, Wallet wallet);
    void save(Expense expense);

    Optional<Expense> findById(int id);
    List<Expense> findAllByWalletOrderByDate(Wallet wallet);
}
